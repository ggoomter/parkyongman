package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration	//이 자바파일은 설정파일로 쓰입니다.
@PropertySource("classpath:/application.properties")  //디폴트가 아닌 설정파일을 우선순위로 읽어올 수 있다.
@EnableTransactionManagement
public class DBConfiguration {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")//설정파일의 있는 property를 자바클래스로 바인딩
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());	//설정파일에서 읽어온 자바설정을 인자로 전달하여 데이터소스리턴
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());	//리턴받은 데이터소스로 팩토리 빈의 데이터소스 설정
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("com.board.*");
		factoryBean.setConfiguration(mybatisConfg());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}