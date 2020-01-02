package com.metro.nccc.migration.model.enums;

import java.util.Objects;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-16 下午2:54
 */
public interface BaseEnum {
    Integer getValue();

    /**
     * 反序列化
     *
     * @param enumType 实际枚举类型
     * @param value    当前值
     * @param <T>      枚举类型并且实现 {@link BaseEnum} 接口
     * @return 枚举常量
     */
    static <T extends Enum<T> & BaseEnum> T valueOf(Class<T> enumType, Object value) {
        if (enumType == null || value == null) {
            return null;
        }

        T[] enumConstants = enumType.getEnumConstants();
        for (T enumConstant : enumConstants) {
            Object enumValue = enumConstant.getValue();
            if (Objects.equals(enumValue, value)
                    || Objects.equals(enumValue.toString(), value.toString())) {
                return enumConstant;
            }
        }

        return null;
    }
}
