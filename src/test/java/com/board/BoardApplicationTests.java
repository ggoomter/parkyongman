package com.board;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SqlSessionFactory sessionFactory;
	
	
	@Test
	void contextLoads() {
	}
	
	
	//어플리케이션콘텍스트에 의한 테스트
	@Test
	public void testByApplicationContext() {
		try {
			System.out.println("=========================");
			System.out.println(context.getBean("sqlSessionFactory"));	//DefaultSqlSessionFactory@3aed69dd
			System.out.println("=========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//빈으로 등록된 Sql세션팩토리에 의한 테스트
	@Test
	public void testBySqlSessionFactory() {
		try {
			System.out.println("=========================");
			System.out.println(sessionFactory.toString());	//DefaultSqlSessionFactory@1ca3d25b
			System.out.println("=========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
