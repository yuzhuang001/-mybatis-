package com.company;

import java.io.IOException;
import java.io.InputStream;

import org.company.mybatis.DefaultSqlSession;
import org.company.mybatis.DefaultSqlSessionFactory;

import com.company.dao.Find;
import com.company.pojo.Emp;

public class TestMybatis {
	public static void main(String[] args) throws IOException {
		
//		String resource="mybatis-config.xml";
//		InputStream resourceAsStream = Resources.getResourceAsStream(resource);
		
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//		SqlSession session = sqlSessionFactory.openSession();
//		Find mapper = session.getMapper(Find.class);
//		Emp findOneById = mapper.findOneById(4);
//		System.out.println(findOneById);
		
		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory();
		DefaultSqlSession session = sqlSessionFactory.openSession();
		Find mapper = session.getMapper(Find.class);
		Emp findOneById = mapper.findOneById(4);
		System.out.println(findOneById);
		
		
		
	}
}
