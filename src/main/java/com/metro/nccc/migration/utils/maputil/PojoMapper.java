package com.metro.nccc.migration.utils.maputil;

import com.metro.nccc.migration.model.po.auth.DepartmentEntity;
import com.metro.nccc.migration.model.vo.usercenter.WechatUserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-15 上午11:35
 */
@Mapper
public interface PojoMapper {
    PojoMapper INSTANCE = Mappers.getMapper(PojoMapper.class);

    @Mappings({
            @Mapping(source = "position", target = "name"),
            @Mapping(source = "department", target = "parentId"),
    })
    DepartmentEntity toDepartment(WechatUserInfo userInfo);
}
