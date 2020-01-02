package com.metro.nccc.migration.service.usercenter;

import com.metro.nccc.migration.dao.auth.AuthMapper;
import com.metro.nccc.migration.dao.usercenter.UsercenterMapper;
import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.po.usercenter.UserEntity;
import com.metro.nccc.migration.model.vo.usercenter.WechatUserInfo;
import com.metro.nccc.migration.utils.ibatis.ArrayObject;
import com.metro.nccc.migration.utils.maputil.PojoMapper;
import com.metro.nccc.migration.utils.response.WechatResponse;
import com.metro.nccc.migration.utils.restclient.WechatHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 上午11:14
 */
public class SyncUserService {
    @Autowired
    RedisTemplate redisTemplate;


    private final String MIGRATION_ACCESSTOKEN = "NCCC:MIGRATION:ACCESSTOKEN";

    public Boolean sync() {

        return true;

    }

    private String getAccessToken() {
        String accessToken = (String) redisTemplate.opsForValue().get(MIGRATION_ACCESSTOKEN);
        if (StringUtils.isEmpty(accessToken)) {
            //TODO:重新获取
        }
        return accessToken;
    }

    class syncUserTask implements Runnable {
        private Long departmentId;
        @Autowired
        private WechatHttpClient wechatHttpClient;
        @Resource
        AuthMapper authMapper;
        @Resource
        UsercenterMapper usercenterMapper;
        @Value("${migration.accesstoken}")
        String accessToken;
        int ferch_child = 0;

        public syncUserTask(Long departmentId) {
            this.departmentId = departmentId;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void run() {
            List<WechatUserInfo> userInfos = simplelist(accessToken, departmentId, ferch_child);
            if(userInfos==null||userInfos.size()==0){
                return;
            }
            List<DepartmentEntity> departmentEntities = insertPositions(userInfos);
            usercenterMapper.batchInsert(userInfos);
            List<UserEntity> userEntities = usercenterMapper.queryByWechatIds(new ArrayObject(userInfos.stream().map(WechatUserInfo::getUserid).collect(Collectors.toList())));
        }

        private List<DepartmentEntity> insertPositions(List<WechatUserInfo> userInfos){
            List<String> positions = userInfos.stream().map(WechatUserInfo::getPosition).distinct().collect(Collectors.toList());
            List<DepartmentEntity> entities = positions.stream().distinct().map(n->new DepartmentEntity(n,departmentId)).collect(Collectors.toList());
            return entities;
        }

        private List<WechatUserInfo> simplelist(String access_token, Long department_id, int fetch_child) {
            WechatResponse<List<WechatUserInfo>> response = wechatHttpClient.simplelist(access_token, department_id, fetch_child);
            if (response.isSuccessed()) {
                return response.getData();
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
