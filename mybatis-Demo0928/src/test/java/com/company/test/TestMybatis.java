package com.company.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.company.mybatis.DefaultExecutor;
import org.company.mybatis.DefaultSqlSession;
import org.company.mybatis.DefaultSqlSessionFactory;
import org.company.mybatis.ReflectionPojoUtils;
import org.junit.Test;

import com.company.dao.Find;
import com.company.pojo.Emp;

public class TestMybatis {

	@Test
	public void testMybatis() {
		DefaultSqlSessionFactory sessionFactory = new DefaultSqlSessionFactory();
		DefaultSqlSession openSession = sessionFactory.openSession();
		Find mapper = openSession.getMapper(Find.class);
		List<Emp> findAll = mapper.findAll();
		for (Emp emp : findAll) {
			System.out.println(emp);
		}
	}
	@Test
	public <E>void testReflectionUtil() {
		Emp emp = new Emp();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql:///yonghedb?characterEncoding=utf-8", "root", "root");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from emp");
//			ReflectionPojoUtils.setValuesIntoPojo(emp, resultSet);
//			System.out.println(emp);
			List<E> list=new ArrayList<E>();
			DefaultExecutor.handlerResultSet(
					resultSet, list, "com.company.pojo.Emp");
			for (E e : list) {
				System.out.println(e);
			}
			
			
			

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					connection = null;
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					resultSet = null;
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					statement = null;
				}
			}
		}

	}

}
