package org.company.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 功能:
 * 1.用于proxy功能更改的实现类 
 * 2.确定proxy代理对象调用session中的哪个方法
 */

public class MapperInvocationHandler implements InvocationHandler {
	private DefaultSqlSession defaultSqlSession;

	public MapperInvocationHandler(DefaultSqlSession defaultSqlSession) {
		// TODO Auto-generated constructor stub
		this.defaultSqlSession = defaultSqlSession;
	}
	
	/**重写invoke方法
	 * @param:proxy 代理类
	 * @param:method 代理工程中要调用的方法(接口的方法)
	 * @param:args 方法需要的若干参数(接口方法中传入的参数)
	 * 底层用的是反射实现的代理功能
	 * 判断调用session中哪个方法的依据:
	 * 1.根据返回值确定调用selectOne还是selsctlist (在return中返回)
	 * 2.根据名字确定sql语句
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		if (Collection.class.isAssignableFrom(method.getReturnType())) {
//			System.out.println(method.getDeclaringClass().getName() + "." + method.getName());
			return defaultSqlSession.selectList(method.getDeclaringClass().getName() + "." + method.getName(),
					args == null ? null : args[0]);

		} else {
//			System.out.println(method.getDeclaringClass().getName() + "." + method.getName());
			return defaultSqlSession.selectOne(method.getDeclaringClass().getName() + "." + method.getName(),
					args == null ? null : args[0]);
		}
	}

}
