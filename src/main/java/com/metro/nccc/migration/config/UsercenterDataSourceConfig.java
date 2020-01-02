package com.metro.nccc.migration.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 上午10:29
 */

@Configuration
@MapperScan(basePackages = "com.metro.nccc.migration.dao.usercenter",sqlSessionFactoryRef = "usercenterDataSourceConfig")
public class UsercenterDataSourceConfig {
    @Primary
    @Bean(name = "usercenterDataSource")
    @ConfigurationProperties("datasource.usercenter")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("usercenterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/usercenter/*.xml"));
        return sessionFactoryBean.getObject();
    }
}
