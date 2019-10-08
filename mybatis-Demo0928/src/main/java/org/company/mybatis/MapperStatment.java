package org.company.mybatis;

/**
 *主要用于保存mapper中的信息,然后放入configruation中的map中保存
 */
public class MapperStatment {
	private String namespace;
	private String id;
	private String sql;
	private String resultType;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	@Override
	public String toString() {
		return "MapperStatment [namespace=" + namespace + ", id=" + id + ", sql=" + sql + ", resultType=" + resultType
				+ "]";
	}

}
