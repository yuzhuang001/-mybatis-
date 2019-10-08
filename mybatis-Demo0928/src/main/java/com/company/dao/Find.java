package com.company.dao;

import java.util.List;

import com.company.pojo.Emp;

public interface Find {
	public List<Emp> findAll();
	public Emp findOneById(Integer id);
}
