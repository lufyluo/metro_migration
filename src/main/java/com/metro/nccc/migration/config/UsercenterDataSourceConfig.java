package com.metro.nccc.migration.config;

import com.metro.nccc.migration.model.enums.BaseEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 上午10:29
 */

@Configuration
@MapperScan(basePackages = "com.metro.nccc.migration.dao.usercenter", sqlSessionFactoryRef = "usercenterSqlSessionFactory")
public class UsercenterDataSourceConfig {
    @Bean(name = "usercenterDataSource")
    @ConfigurationProperties("spring.datasource.usercenter")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "usercenterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("usercenterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeHandlers(new BaseEnumTypeHandler(BaseEnum.class));
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mybatis/mapper/usercenter/*.xml"));
        return sessionFactoryBean.getObject();
    }

    @Bean("usercenterSqlSessionTemplate")
    public SqlSessionTemplate test1sqlsessiontemplate(
            @Qualifier("usercenterSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
