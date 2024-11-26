package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Service;

public interface DAO<T> {
	T get(int id) ;
	
	Map<Integer,T> getAll() ;
	
	int insert(T t) ;
	
	int update(T t) ;
	
	int delete(T t);
	
}
