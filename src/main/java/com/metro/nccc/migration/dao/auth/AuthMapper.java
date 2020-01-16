package com.metro.nccc.migration.dao.auth;

import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.po.auth.DepartmentUserEntity;
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

    List<DepartmentEntity> pageQuery(int pageIndex, int pageSize);

    List<DepartmentEntity> queryUpper(Long parentId);

    Integer batchInsertDepartUser(List<DepartmentUserEntity> departmentUserEntities);

    Integer countDeaprt();

}
