package com.metro.nccc.migration.model.po.auth;

import lombok.Data;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-19 下午3:35
 */
@Data
public class UserRoleEntity{
    private Long id;
    private Long userId;
    private String employeeNo;
    private Integer[] roleIds;
}
