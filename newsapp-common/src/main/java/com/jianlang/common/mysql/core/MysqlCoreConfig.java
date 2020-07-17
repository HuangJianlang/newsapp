package com.jianlang.common.mysql.core;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mysql.core")
@PropertySource("classpath:mysql-core-jdbc.properties")
@MapperScan(basePackages="com.jianlang.model.mappers", sqlSessionFactoryRef = "mysqlCoreSessionFactory")
public class MysqlCoreConfig {

    String jdbcUrl;
    String jdbcUserName;
    String jdbcPassword;
    String jdbcDriver;
    String rootMapper;
    //别包名
    String aliasesPackage;
    /**
     * 数据库连接池
     */
    @Bean("mysqlCoreDataSource")
    public DataSource mysqlCoreDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        //获取配置文件中的username
        dataSource.setJdbcUrl(this.getJdbcUrl());
        dataSource.setDriverClassName(this.getJdbcDriver());
        dataSource.setUsername(this.getJdbcUserName());
        dataSource.setPassword(this.getJdbcPassword());

        dataSource.setMaximumPoolSize(50);
        dataSource.setMinimumIdle(5);

        return dataSource;
    }

    /**
     * Mybaits
     */
    //注入
    @Bean("mysqlCoreSessionFactory")
    public SqlSessionFactoryBean mysqlCoreSessionFactory(@Qualifier("mysqlCoreDataSource") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        //data source
        factoryBean.setDataSource(dataSource);
        //别名
        factoryBean.setTypeAliasesPackage(this.getAliasesPackage());
        //mapper相关
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources(this.getMapperFile()));

        //xxx_yyy -> xxxYyy
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        return factoryBean;
    }

    public String getRealPassword(){
        String password = StringUtils.reverse(this.getJdbcPassword());
        return password;
    }

    public String  getMapperFile(){
        return new StringBuffer("classpath:").append(this.getRootMapper()).append("/**/*.xml").toString();
    }
}
