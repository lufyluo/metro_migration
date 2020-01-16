package com.metro.nccc.migration.model.vo.usercenter;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午2:45
 */
@Data
public class WechatUserInfo {
    private Long id;
    /**
     * 企业微信userid，在本系统中对应erp_wechatid和employee_no
     * */
    private String userid;
    private String name;
    private Long[] department;

    /**
     * department : [1,2]
     * order : [1,2]
     * position : 后台工程师
     * mobile : 15913215421
     * gender : 1
     * email : zhangsan@gzdev.com
     * is_leader_in_dept : [1,0]
     * avatar : http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0
     * thumb_avatar : http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/100
     * telephone : 020-123456
     * enable : 1
     * alias : jackzhang
     * status : 1
     * address : 广州市海珠区新港中路
     * hide_mobile : 0
     * english_name : jacky
     */

    private String position;
    private String mobile;
    private Integer gender;
    private String email;
    private String avatar;
    private String thumb_avatar;
    private String telephone;
    private int enable;
    private String alias;
    private int status;
    private String address;
    private int hide_mobile;
    private String english_name;
    private List<Integer> order;
    private List<Integer> is_leader_in_dept;

}
