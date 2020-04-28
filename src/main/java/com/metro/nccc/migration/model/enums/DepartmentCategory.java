package com.metro.nccc.migration.model.enums;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-10-24 下午2:35
 */
public enum DepartmentCategory implements BaseEnum {

    HEADCOMPANY("总公司"), BRANCHCOMPANY("分公司"), DEPARTMENT("部门"), POSITION("岗位"),GROUP("集团总公司"),STATIONAREA("站区");

    private String desc;

    DepartmentCategory(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }}
