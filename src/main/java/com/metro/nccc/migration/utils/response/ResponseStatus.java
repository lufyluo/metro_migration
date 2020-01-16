package com.metro.nccc.migration.utils.response;


import com.metro.nccc.migration.model.enums.BaseEnum;

import java.util.Objects;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-14 下午5:28
 */
public enum ResponseStatus implements BaseEnum {
    /*
     *操作成功
     */
    SUCCESS("00000000", "成功"),
    FAILURE("00000001", "发生异常"),
    UNAUTHORIZED("00000002", "鉴权失败"),
    ARGUMENTNOTVALID("00000003", "参数异常"),
    WECHATSUCCESS("0","ok"),
    WECHATCODEEXCEPTION("00000004", "获取企业微信code错误");

    private String code;
    private String desc;

    ResponseStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResponseStatus valueOfCode(String code) {

        ResponseStatus[] enumConstants = ResponseStatus.values();
        for (ResponseStatus enumConstant : enumConstants) {
            Object enumValue = enumConstant.getCode();
            if (Objects.equals(enumValue, code)) {
                return enumConstant;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
