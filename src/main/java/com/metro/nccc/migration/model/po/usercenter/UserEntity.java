package com.metro.nccc.migration.model.po.usercenter;

import lombok.Data;

import java.util.Date;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-28 上午9:33
 */
@Data
public class UserEntity{
    private Long id;
    private String name;
    private String employeeNo;
    private String password = "e10adc3949ba59abbe56e057f20f883e";
    private String erpWechatId;
    private String idNo;
    private String mobile;
    private int age;
    private String organization;
    private String address;
    private Date entryDate;
    private Date employDate;
    private Date birthday;
    private String avatar;
}
