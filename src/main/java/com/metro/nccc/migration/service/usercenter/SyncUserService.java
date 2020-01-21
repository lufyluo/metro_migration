package com.metro.nccc.migration.service.usercenter;

import com.metro.nccc.migration.dao.auth.AuthMapper;
import com.metro.nccc.migration.dao.usercenter.UsercenterMapper;
import com.metro.nccc.migration.model.MigrationException;
import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.po.auth.DepartmentUserEntity;
import com.metro.nccc.migration.model.po.usercenter.UserEntity;
import com.metro.nccc.migration.model.vo.usercenter.WechatUserInfo;
import com.metro.nccc.migration.utils.ibatis.ArrayObject;
import com.metro.nccc.migration.utils.maplain.MaplainUtil;
import com.metro.nccc.migration.utils.response.BaseResponse;
import com.metro.nccc.migration.utils.response.ResponseStatus;
import com.metro.nccc.migration.utils.response.WechatResponse;
import com.metro.nccc.migration.utils.restclient.BasicdataService;
import com.metro.nccc.migration.utils.restclient.WechatHttpClient;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 上午11:14
 */
@Service
public class SyncUserService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private WechatHttpClient wechatHttpClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    BasicdataService basicdataService;
    @Resource
    AuthMapper authMapper;
    @Resource
    UsercenterMapper usercenterMapper;
    @Autowired
    @Qualifier("fixedPool")
    ExecutorService executor;

    private final String MIGRATION_ACCESSTOKEN = "NCCC:MIGRATION:ACCESSTOKEN";

    @Value("${migration.agentId}")
    private String agentId;
    @Value("${migration.appId}")
    private String appId;
    @Value("${migration.accesstoken}")
    private String accessTokenDefault;
    private volatile AtomicReference<String> accesstoken = new AtomicReference<>();
    private volatile AtomicBoolean flag = new AtomicBoolean(true);
    Lock lock = new ReentrantLock();

    public Boolean sync(){
        int count = authMapper.countDeaprt();
        System.out.println("部门总数： " + count);
        int index = 0;
        flag.set(false);
        String result = getWechatCode();
        if(StringUtils.isEmpty(accesstoken.get())){
            throw new MigrationException(result);
        }
        while (index < count) {
            List<DepartmentEntity> departmentEntities = authMapper.pageQuery(index, 1);
            departmentEntities.forEach(n -> {
                syncUserTask task = new syncUserTask(n.getId());
                executor.execute(task);
            });
            //executor.awaitTermination(1000, TimeUnit.SECONDS);
            index++;
            List<Long> ids = departmentEntities.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
            System.out.println(ArrayUtils.toString(ids) + "-------- 刷新完成");
        }

        return true;

    }

    public Boolean async(int index, int count) {
        int total = count + index;
        while (index < total) {
            List<DepartmentEntity> departmentEntities = authMapper.pageQuery(index, 1);
            departmentEntities.forEach(n -> {
                syncUserTask task = new syncUserTask(n.getId());
                task.run();
            });
            index++;
            List<Long> ids = departmentEntities.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
            System.out.println(ArrayUtils.toString(ids) + "-------- 刷新完成");
        }

        return true;

    }
    private String getWechatCode() {
        if (!flag.get()) {
            try {
                lock.lock();
                if(flag.get())
                {
                    return accesstoken.get();
                }
                BaseResponse<String> response = basicdataService.accessToken(agentId, appId, System.currentTimeMillis() + "");
                if (ResponseStatus.SUCCESS.getCode().equals(response.getCode())) {
                    System.out.println("获得accesstoken： " + response.getData());
                    accesstoken.set(response.getData());
                }
            }catch (Exception ex){
                return ex.getMessage();
            }
            finally {
                flag.set(true);
                lock.unlock();
            }
        }
        return accesstoken.get();
    }

    class syncUserTask implements Runnable {
        private Long departmentId;

        int ferch_child = 0;

        public syncUserTask(Long departmentId) {
            this.departmentId = departmentId;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void run() {
            List<WechatUserInfo> userInfos = simplelist(accesstoken.get(), departmentId, ferch_child);
            if (userInfos == null || userInfos.size() == 0) {
                return;
            }
            fixUserId(userInfos);
            userInfos = userInfos.stream().filter(n -> n.getId() != null).collect(Collectors.toList());
            if (userInfos == null || userInfos.size() == 0) {
                return;
            }
            List<DepartmentEntity> positions = insertPositions(userInfos);
            insertUserPositions(userInfos, positions);
        }

        private void fixUserId(List<WechatUserInfo> userInfos) {
            List<String> wechatIds = userInfos.stream().map(WechatUserInfo::getUserid).collect(Collectors.toList());
            List<UserEntity> userEntities = usercenterMapper.queryByWechatIds(wechatIds);
            if (userEntities == null || userEntities.size() == 0 || userInfos.size() > userEntities.size()) {
                List<WechatUserInfo> newUsers = userInfos.stream().filter(
                        n -> !userEntities.stream()
                                .anyMatch(userEntity -> userEntity.getErpWechatId().equals(n.getUserid())))
                        .collect(Collectors.toList());
                usercenterMapper.batchInsert(newUsers);
                List<UserEntity> newEntities = newUsers.stream().map(item -> {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(item.getId());
                    userEntity.setErpWechatId(item.getUserid());
                    return userEntity;
                }).collect(Collectors.toList());
                userEntities.addAll(newEntities);
            }else{
                userEntities.forEach(user->{
                    Optional<WechatUserInfo> optional = userInfos.stream().filter(n->n.getUserid().equals(user.getErpWechatId())).findFirst();
                    if(optional.isPresent()){
                        user.setAvatar(optional.get().getAvatar());
                    }

                });

                usercenterMapper.updateAvatar(userEntities);
            }
            fillId(userInfos, userEntities);
        }

        private void fillId(List<WechatUserInfo> userInfos, List<UserEntity> userEntities) {
            userInfos.forEach(n -> {
                Optional<UserEntity> optional = userEntities.stream().filter(user -> ObjectUtils.equals(user.getErpWechatId(), n.getUserid())).findFirst();
                if (optional.isPresent()) {
                    n.setId(optional.get().getId());
                }
            });
        }

        private void insertUserPositions(List<WechatUserInfo> userInfos, List<DepartmentEntity> departmentEntities) {
            List<DepartmentEntity> upDepartments = authMapper.queryUpper(departmentId);
            List<DepartmentEntity> upSortDepartments = MaplainUtil.flat(MaplainUtil.toHierarchy(upDepartments));
            List<Integer> paths = upSortDepartments.stream().map(value -> value.getId().intValue()).collect(Collectors.toList());

            List<DepartmentUserEntity> departmentUserEntities = buildDepartUserEntities(userInfos, departmentEntities, paths);
            updateIfExist(departmentUserEntities);
            if(departmentUserEntities == null || departmentUserEntities.size()==0){
                return;
            }
            authMapper.batchInsertDepartUser(departmentUserEntities);

        }

        private void updateIfExist(List<DepartmentUserEntity> departmentUserEntities) {
            ArrayObject arrayObject = new ArrayObject(departmentUserEntities.stream().map(DepartmentUserEntity::getUserId).collect(Collectors.toList()));
            List<DepartmentUserEntity> exist = authMapper.queryUserDeparts(arrayObject);
            if (exist == null || exist.size() == 0) {
                return;
            }
//            List<DepartmentUserEntity> needUpdateData = departmentUserEntities
//                    .stream()
//                    .filter(n -> exist.stream().anyMatch(existEntity -> existEntity.getUserId().equals(n.getUserId())))
//                    .collect(Collectors.toList());
            List<DepartmentUserEntity> needUpdateData = new ArrayList<>();
            departmentUserEntities.forEach(item -> {
                Optional<DepartmentUserEntity> optional = exist.stream().filter(n -> n.getUserId().equals(item.getUserId())).findFirst();
                if (optional.isPresent()) {
                    //Arrays.stream(item.getDepartmentPath()).collect(Collectors.toList()).addAll(optional.get().getDepartmentPath());
                    Integer[] path = Arrays.stream(ArrayUtils.addAll(item.getDepartmentPath(), optional.get().getDepartmentPath())).distinct().toArray(Integer[]::new);
                    item.setDepartmentPath(path);
                    needUpdateData.add(item);
                }
            });
            if (needUpdateData.size() > 0) {
                authMapper.updatePath(needUpdateData);
                departmentUserEntities.removeAll(needUpdateData);
            }
        }

        private List<DepartmentUserEntity> buildDepartUserEntities(List<WechatUserInfo> userInfos, List<DepartmentEntity> departmentEntities, List<Integer> paths) {
            List<DepartmentUserEntity> departmentUserEntities = userInfos.stream().map(n -> {
                DepartmentUserEntity departmentUserEntity = new DepartmentUserEntity();
                Long positionId = findPositionId(n, departmentEntities);
                List<Integer> newPaths = paths.stream().filter(path -> path != null).collect(Collectors.toList());
                if (positionId != null) {

                    newPaths.add(positionId.intValue());
                    departmentUserEntity.setDepartmentId(positionId);
                } else {
                    departmentUserEntity.setDepartmentId(paths.get(paths.size() - 1).longValue());
                }
                //Integer[] pathArr =paths.stream().toArray(Integer[]::new);
                Integer[] pathArr = newPaths.toArray(new Integer[0]);
                departmentUserEntity.setDepartmentPath(pathArr);
                departmentUserEntity.setUserId(n.getId());
                return departmentUserEntity;
            }).collect(Collectors.toList());
            return departmentUserEntities;
        }

        private Long findPositionId(WechatUserInfo wechatUserInfo, List<DepartmentEntity> departmentEntities) {
            Optional<DepartmentEntity> optional = departmentEntities.stream().filter(n -> n.getName().equals(wechatUserInfo.getPosition())).findFirst();
            if (optional.isPresent()) {
                return optional.get().getId();
            } else {
                return null;
            }
        }

        private List<DepartmentEntity> insertPositions(List<WechatUserInfo> userInfos) {
            List<String> positions = userInfos.stream().filter(n -> !StringUtils.isEmpty(n.getPosition())).map(WechatUserInfo::getPosition).distinct().collect(Collectors.toList());
            if (positions == null || positions.size() == 0) {
                return new ArrayList<>();
            }
            List<DepartmentEntity> entities = positions.stream().distinct().map(n -> new DepartmentEntity(n, departmentId)).collect(Collectors.toList());
            authMapper.batchInsertPosition(entities);
            return entities;
        }

        private List<WechatUserInfo> simplelist(String access_token, Long department_id, int fetch_child) {
            WechatResponse response = wechatHttpClient.list(access_token, department_id, fetch_child);
            if (response.isSuccessed()) {
                return response.getUserlist();
            } else if (response.notAuthed()) {
                return new ArrayList<>();
            } else if (response.departmentRemoved()) {
                authMapper.delete(department_id);
            } else {
                flag.set(false);
                accesstoken.set(getWechatCode());
            }
            return new ArrayList<>();

        }



        public Long getDepartmentId() {
            return departmentId;
        }

        @Override
        public String toString() {
            return "Task [departmentId=" + departmentId + "]";
        }
    }

}
