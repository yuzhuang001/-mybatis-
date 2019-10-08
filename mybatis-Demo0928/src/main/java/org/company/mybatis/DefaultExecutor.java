package org.company.mybatis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.company.pojo.Emp;

/**业务功能: 
 * 
 * 1.建立连接,具体执行查询功能 
 * 2.把查询出来的数据库数据通过反射封装成对象数据 
 * 3.处理占位符
 */
public class DefaultExecutor {
	private final Configurations configurations;

	public DefaultExecutor(Configurations configurations) {
		// TODO Auto-generated constructor stub
		this.configurations = configurations;
	}

	public <E> List<E> query(MapperStatment ms, Object parameter) {
//		System.out.println(ms.getSql());
		List<E> list = new ArrayList<E>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = configurations.getJdbcUrl();
			String user = configurations.getUsername();
			String password = configurations.getPassword();
			connection = DriverManager.getConnection(url, user, password);
			String sql = ms.getSql();
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			// 简单的处理下sql语句
			prepareStatementParameter(prepareStatement, parameter);
			resultSet = prepareStatement.executeQuery();
			List<E> list1 = new ArrayList<E>();
			// 封装数据并输出到控制台
			handlerResultSet(resultSet, list1, "com.company.pojo.Emp");
			for (E e : list1) {
				System.out.println(e);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 关闭资源的一系列操作
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					// 最后实在关闭不了,那就由GC回收
					connection = null;
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					// 最后实在关闭不了,那就由GC回收
					resultSet = null;
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					// 最后实在关闭不了,那就由GC回收
					statement = null;
				}
			}
		}
		return list;
	}

	/**
	 * 功能:简单的处理sql语句把占位符换成传来的参数
	 * (真实的估计是用Map或者object对象实现的)
	 * @param prepareStatement prepareStatement的sql语句
	 * @param parameter        动态sql传过来的参数
	 * @throws SQLException
	 */
	public void prepareStatementParameter(PreparedStatement prepareStatement, Object parameter) throws SQLException // 此处阉割的厉害
	{
		if (parameter instanceof Integer) {
			prepareStatement.setInt(1, (Integer) parameter);
		} else if (parameter instanceof String) {
			prepareStatement.setString(1, (String) parameter);
		} else if (parameter instanceof String) {
			prepareStatement.setDouble(1, (Double) parameter);
		}
	}

	/**
	 * 遍历resultSet结果集并逐一封装成指定对象
	 * 
	 * @param resultSet 结果集
	 * @param list      存封装好的若干个对象
	 * @param className 想要封装成的对象的全路径
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static <E> void handlerResultSet(ResultSet resultSet, List<E> list, String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (resultSet.next()) {
				try {
					/**
					 * 注意!!! 这里的object不能放在循环外边,也就是说必须每次循环都要new一个新对象 若果共享一个object变量的话会造成形参对实参的修改
					 * 最后的结果就是查出的所有结果都是一样的(新结果覆盖了原先的结果)
					 */
					Object object = clazz.newInstance();
					ReflectionPojoUtils.setValuesIntoPojo(object, resultSet);
					list.add((E) object);
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
