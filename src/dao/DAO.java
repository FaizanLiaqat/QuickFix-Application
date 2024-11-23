package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Service;

public interface DAO<T> {
	T get(int id) throws SQLException;
	
	Map<Integer,T> getAll() throws SQLException;
	
	int insert(T t) throws SQLException;
	
	int update(T t) throws SQLException;
	
	int delete(T t) throws SQLException;
	
}
