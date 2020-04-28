package com.metro.nccc.migration.dao.auth;

import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.po.auth.DepartmentUserEntity;
import com.metro.nccc.migration.model.po.auth.UserRoleEntity;
import com.metro.nccc.migration.utils.ibatis.ArrayObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-10-18 下午3:26
 */
@Mapper
public interface AuthMapper {

    List<DepartmentEntity> query(DepartmentEntity departmentEntity);

    Integer update(DepartmentEntity entity);

    Integer batchInsertPosition(@Param("list") List<DepartmentEntity> entities);

    Integer insert(DepartmentEntity entities);

    List<DepartmentEntity> pageQuery(int pageIndex, int pageSize);

    List<DepartmentEntity> queryUpper(Long parentId);

    Integer batchInsertDepartUser(List<DepartmentUserEntity> departmentUserEntities);

    Integer countDeaprt();

    Integer delete(Long departmentId);

    List<DepartmentUserEntity> queryUserDeparts(@Param("userIds") ArrayObject userIds);

    Integer updatePath(@Param("needUpdateData") List<DepartmentUserEntity> needUpdateData);

    UserRoleEntity queryUserRole(Long userId);

    void insertUserRole(Long userId, String employeeNo);

    void updateUserRole(Long id, String employeeNo);
}
