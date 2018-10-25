package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable{
	
	void save(T obj) throws Exception;
	void persist(T obj) throws Exception;
	void saveOrUpdate(T obj) throws Exception;
	void update(T obj) throws Exception;
	void delete(T obj) throws Exception;
	T merge(T obj) throws Exception;
	List<T> findList(Class<T> objs) throws Exception;
	Object findById(Class<T> entidade, long id) throws Exception;
	T findPorId(Class<T> entidade, long id) throws Exception;
	List<T> findListByQueryDinamica(String s)throws Exception;
	void executeUpdateQueryDinamica(String s)throws Exception;
	void executeUpdateSQLDinamica(String s)throws Exception;
	void clearSession() throws Exception;
	void evict(Object objs) throws Exception;
	Session getSession() throws Exception;
	List<?> getListSQLDinamica(String sql) throws Exception;
	JdbcTemplate getJdbcTemplate();
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	SimpleJdbcInsert getSimpleJdbcInsert();
	Long totalRegistro(String table) throws Exception;
	Query obterQuery(String query)throws Exception;
	
	List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado)throws Exception;
      
}
