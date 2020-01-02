package com.metro.nccc.migration.model.po.auth;

import com.metro.nccc.migration.model.enums.DepartmentCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-10-18 下午3:24
 */
@Getter
@Setter
@NoArgsConstructor
public class DepartmentEntity {
    private Long id;
    private String name;
    private Long parentId;
    private DepartmentCategory type;
    private Integer order;
    private String code;
    private Boolean leader;
    private Integer[] departMentPath;
    private Date createdTime;
    private Date updatedTime;

    public DepartmentEntity(String position, Long departmentId) {
        this.name = position;
        this.parentId = getParentId();
    }
}
