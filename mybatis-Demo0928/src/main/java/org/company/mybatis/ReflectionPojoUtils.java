package org.company.mybatis;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.chainsaw.Main;

import com.company.pojo.Emp;

public class ReflectionPojoUtils {
	/**
	 * @author YZ
	 * @paramter:Object object,写到指定的对象
	 * @paramter:String attributeName,对象的属性名
	 * @paramter:Object value,对象的属性值
	 */
	public static void setValuesIntoField(Object object, String attributeName, Object value) {
		Field field;
		try {
			field = object.getClass().getDeclaredField(attributeName);
			field.setAccessible(true);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			System.out.println("--无此属性--");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author YZ
	 * @throws SQLException
	 * @paramter:Object object,写到指定的对象
	 * @paramter:ResultSet resultSet,结果集合
	 */
	public static void setValuesIntoPojo(Object object, ResultSet resultSet) throws SQLException {
		/**
		 * 异常：java.sql.SQLException: Before start of result set
		 * 解决方法：使用rs.getString();前一定要加上rs.next();
		 * 原因：ResultSet对象代表SQL语句执行的结果集，维护指向其当前数据行的光标。
		 * 每调用一次next()方法，光标向下移动一行。最初它位于第一行之前，因此第一次调用next()应把光标置于第一行上，使它成为当前行。随着每次调用next()将导致光标向下移动一行。
		 * 在ResultSe对象及其t父辈Statement对象关闭之前，光标一直保持有效。(resultSet的每条表记录相当于一个pojo对象)
		 * 
		 * @param:Object object指定pojo
		 */
//		resultSet.next();// 注意这里,测试时要加上此语句
		Field[] declaredFields = object.getClass().getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			setValuesIntoField(object, declaredFields[i].getName(), resultSet.getObject(declaredFields[i].getName()));
			//注意这里resultSet是一个结果集,通过next方法改变光标指向,这里可以把它当成一个单个的结果来处理,调用此方法的函数通过next方法遍历整个结果集
		}
	}
}
