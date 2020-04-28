package com.metro.nccc.migration.service.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.metro.nccc.migration.model.vo.excel.EmployerExcel;
import com.metro.nccc.migration.service.excelImport.ExcelHandService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-3-26 上午10:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentExcelImportTest {
    @Autowired
    private ExcelHandService excelHandService;
    @Test
    public void test2() {
        ImportParams params = new ImportParams();
        params.setStartRows(0);
        List<EmployerExcel> list = ExcelImportUtil.importExcel(
                new File(System.getProperty("user.dir")+"/group.xlsx"),
                EmployerExcel.class, params);
        excelHandService.hand(list);
    }

    @Test
    public void stringTest() {
        String str = "成都轨道交通集团有限公司/运营公司/运营一分公司/锦城湖站区/九兴大道";
        String last = str.substring(str.lastIndexOf("/")+1);
        String path = str.substring(0,str.lastIndexOf("/"));
        System.out.println(last+"/n"+path);
    }
}
