package org.company.mybatis;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 业务功能: 
 * 1.执行查询语句的命令,有若干个查询方法把请求判断处理转发给Executor执行
 * 2.有一个getMapper方法,返回一个接口的实现对象,使用jdk自带的动态代理API.
 */
public class DefaultSqlSession {
	private final Configurations configurations;
	private final DefaultExecutor defaultExecutor;

	public DefaultSqlSession(Configurations configurations) {
//		super();
		this.configurations = configurations;// 构造方法注入变量
		this.defaultExecutor = new DefaultExecutor(configurations);// 组合defaultExecutor以便使用其功能.
	}

	// selectOne也是调用selectList,然后返回一个容量为1的数组
	@SuppressWarnings("unchecked")
	public <T> T selectOne(String statement, Object parameter) {
		List<Object> selectList = selectList(statement, parameter);
		// 这个判断必不可少,然后return null也是必须的用于跳出方法,可以使方法不报空指针
		if (selectList == null || selectList.size() == 0) // 这里去掉||selectList.size()==0为何就是deadcode
		{
			return null;
		}
		if (selectList.size() == 1) {
			return (T) selectList.get(0);
		} else {// 如果查到多个结果抛异常
			throw new RuntimeException("too many results,please use selectList function!");
		}

	}

	/**
	 * @param statement namespace+id(坐标)
	 * @param parameter 代替占位符的参数
	 * @return 一个泛型列表
	 */
	// selectList
	public <E> List<E> selectList(String statement, Object parameter) {
		return defaultExecutor.query(configurations.getMap().get(statement), parameter);
	}

	/**
	 * @param type 接口的实现类class文件
	 * @return 返回一个代理对象
	 */
	// 动态代理,实现mybatis的接口化编程!(重要!)
	@SuppressWarnings("unchecked") // type就是Find接口
	public <T> T getMapper(Class<T> type) {
		MapperInvocationHandler m = new MapperInvocationHandler(this);
		return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, m);
		/**
		 * 上面有三个参数:
		 * 1.type.getClassLoader():因为是动态代理是根据字节码文件生成代理对象的,需要被代理的类的class文件对象,这里应该是调用getClassLoader方法的原因
		 * 2.new Class[] { type }:代理对象实现的接口,可以是多个,用一个Class[]{}数组放置
		 * 3.m对象是InvocationHandler的实现对象,作用是对原来的被代理的对象通过这个类进行更改,因为每次使用到代理类都会调用InvocationHandler里边的invoke方法
		 */
	}

}
