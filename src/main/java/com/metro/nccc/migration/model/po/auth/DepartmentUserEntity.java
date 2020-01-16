package com.metro.nccc.migration.model.po.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-10-24 下午3:31
 */
@Getter
@Setter
public class DepartmentUserEntity{
    private Long id;
    private Long departmentId;
    private Long userId;
    private Integer[] departmentPath;
}
