package com.metro.nccc.migration.model.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-3-26 上午10:56
 */
@Data
public class EmployerExcel  implements java.io.Serializable {
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "帐号")
    private String employeeNo;
    @Excel(name = "职务")
    private String positions;
    @Excel(name = "部门")
    private String departmentPath;
    @Excel(name = "性别",replace = { "男_0", "女_1" })
    private int sex;

}
