package org.company.mybatis;

import java.util.HashMap;
import java.util.Map;
/**
 *功能:存储所有的配置信息包括
 *1.jdbc链接的信息
 *2.Mappers文件映射的信息(namespace+id+resultType+SQL语句等)
 */

public class Configurations {
	private String jdbcDriver;
	private String jdbcUrl;
	private String username;
	private String password;

	//map中存了若干个保存sql信息的MapperStatment对象,key是namespace+id
	Map<String, MapperStatment> map = new HashMap<String, MapperStatment>();

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Configurations [jdbcDriver=" + jdbcDriver + ", jdbcUrl=" + jdbcUrl + ", username=" + username
				+ ", password=" + password + ", map=" + map + "]";
	}

	public Map<String, MapperStatment> getMap() {
		return map;
	}

	public void setMap(Map<String, MapperStatment> map) {
		this.map = map;
	}

}
