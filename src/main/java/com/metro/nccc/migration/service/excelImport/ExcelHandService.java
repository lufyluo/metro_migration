package com.metro.nccc.migration.service.excelImport;

import com.metro.nccc.migration.dao.auth.AuthMapper;
import com.metro.nccc.migration.dao.usercenter.UsercenterMapper;
import com.metro.nccc.migration.model.enums.DepartmentCategory;
import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.po.auth.DepartmentUserEntity;
import com.metro.nccc.migration.model.po.usercenter.UserEntity;
import com.metro.nccc.migration.model.vo.excel.EmployerExcel;
import com.metro.nccc.migration.utils.DepartTypeConfig;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-3-26 下午3:21
 */
@Service
public class ExcelHandService {
    @Resource
    private UsercenterMapper usercenterMapper;
    @Resource
    private AuthMapper authMapper;

    private String positionJoinStr = "p_";

    public void hand(List<EmployerExcel> list) {
        int total = list.size();
        final int[] begin = {1};
        list.forEach(n -> {
            fetchDataAndInsert(n);
            System.out.println(String.format("进度：%s/%s,完成用户：%s ,员工号：%s 数据导入", begin[0], total, n.getName(), n.getEmployeeNo()));
            begin[0]++;
        });
    }

    private void fetchDataAndInsert(EmployerExcel employerExcel) {
        UserEntity userEntity = fetchUserAndInsert(employerExcel);
        List<DepartmentEntity> departmentEntities = fetchDepartAndInsert(employerExcel.getPositions(), employerExcel.getDepartmentPath());
        insertUserDepart(userEntity.getId(), departmentEntities);
    }

    private void insertUserDepart(Long userId, List<DepartmentEntity> departmentEntities) {
        DepartmentUserEntity departmentUserEntity = new DepartmentUserEntity();
        departmentUserEntity.setUserId(userId);
        List<Integer> path = departmentEntities.stream().map(value -> value.getId().intValue()).distinct().collect(Collectors.toList());
        departmentUserEntity.setDepartmentPath(path.toArray(new Integer[0]));
        departmentUserEntity.setUserId(userId);
        departmentUserEntity.setDepartmentId(departmentEntities.get(departmentEntities.size() - 1).getId());
        authMapper.batchInsertDepartUser(Arrays.asList(departmentUserEntity));
    }


    private UserEntity fetchUserAndInsert(EmployerExcel employerExcel) {
        UserEntity userEntity = usercenterMapper.queryByEmployeeNo(employerExcel.getEmployeeNo());
        if (userEntity == null || userEntity.getId() == null || userEntity.getId() <= 0) {
            userEntity = new UserEntity();
            userEntity.setErpWechatId(employerExcel.getEmployeeNo());
            userEntity.setName(employerExcel.getName());
            userEntity.setGender(employerExcel.getSex());
            userEntity.setEmployeeNo(employerExcel.getEmployeeNo());
            usercenterMapper.insert(userEntity);
        }
        return userEntity;

    }

    private List<DepartmentEntity> fetchDepartAndInsert(String positions, String departments) {
        String[] positionsNotInSameDepart = StringUtils.isNotEmpty(positions) ? positions.split("，") : new String[0];
        String[] departsNotInSameDepart = departments.split(";");
        List<String> departs = allPositionWithAllPath(positionsNotInSameDepart, departsNotInSameDepart);
        return insertDeparts(departs);
    }

    private List<DepartmentEntity> insertDeparts(List<String> departs) {
        List<DepartmentEntity> departmentEntities = new ArrayList<>();
        departs.forEach(n -> {
            String[] itemDeparts = n.split("/");
            if (ArrayUtils.isNotEmpty(itemDeparts)) {
                departmentEntities.addAll(insertEachDepart(itemDeparts));
            }
        });
        return departmentEntities;
    }

    private List<DepartmentEntity> insertEachDepart(String[] itemDeparts) {
        String currentPath = "";
        Long parentId = null;
        List<DepartmentEntity> departments = new ArrayList<>();
        for (int i = 0; i < itemDeparts.length; i++) {
            DepartmentEntity temp = new DepartmentEntity();
            if (itemDeparts[i].contains(positionJoinStr)) {
                itemDeparts[i] = itemDeparts[i].replace(positionJoinStr, "");
                temp.setType(DepartmentCategory.POSITION);
            }else{
                DepartmentCategory departmentCategory =  DepartTypeConfig.departConfig.get(itemDeparts[i]);
                if(departmentCategory == null){
                    temp.setType(DepartmentCategory.DEPARTMENT);
                }else{
                    temp.setType(departmentCategory);
                }

            }
            currentPath += "/" + itemDeparts[i];
            temp.setName(itemDeparts[i]);
            temp.setPath(currentPath);
            temp.setParentId(parentId);
            List<DepartmentEntity> departmentEntities = authMapper.query(temp);
            if (departmentEntities == null || departmentEntities.size() == 0) {
                authMapper.insert(temp);
                parentId = temp.getId();
                departments.add(temp);
            } else {
                parentId = departmentEntities.get(0).getId();
                departments.add(departmentEntities.get(0));
            }
        }
        return departments;
    }

    public List<String> allPositionWithAllPath(String[] positionsNotInSameDepart, String[] departsNotInSameDepart) {
        if (ArrayUtils.isEmpty(departsNotInSameDepart)) {
            return null;
        }
        List<String> departs = new ArrayList<>();
        for (int i = 0; i < departsNotInSameDepart.length; i++) {
            String path = departsNotInSameDepart[i];

            String lastDepart =path.substring(path.lastIndexOf("/")+1);
            if(DepartTypeConfig.stations.contains(lastDepart)){
                path = path.substring(0,path.lastIndexOf("/"));
            }

            if (ArrayUtils.isNotEmpty(positionsNotInSameDepart) && i < positionsNotInSameDepart.length) {
                String position = positionsNotInSameDepart[i];
                String[] posistions = position.split("、");
                departs.addAll(concatPositions(path, posistions));
            } else {
                departs.add(path);
            }
        }
        return departs;
    }

    private List<String> concatPositions(String depart, String[] posistions) {
        List<String> concatPositions = new ArrayList<>();
        for (String posistion : posistions) {
            concatPositions.add(depart + "/" + positionJoinStr + posistion);
        }
        return concatPositions;
    }

    private void fetchUserDepartAndInsert(EmployerExcel employerExcel) {

    }
}
