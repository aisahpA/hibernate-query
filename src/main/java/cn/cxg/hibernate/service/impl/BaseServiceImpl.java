package cn.cxg.hibernate.service.impl;

import cn.cxg.hibernate.dao.IBaseDao;
import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import cn.cxg.hibernate.service.IBaseService;
import org.hibernate.HibernateException;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Transactional
public class BaseServiceImpl implements IBaseService {

	@Resource(name = "baseDao")
	protected IBaseDao baseDao;

	@Override
	public <T> void delete(T entity) {
		baseDao.delete(entity);
	}

	@Override
	public <T> void deleteAllEntities(Collection<T> entities) {
		baseDao.deleteObjects(new ArrayList<T>(entities));
	}

	@Override
	public <T> void deleteEntityById(Class<T> entityClass, Serializable id) {
		baseDao.delete(baseDao.get(entityClass, id));
	}

	@Override
	public int executeSql(String sql, List<Object> param) {
		return baseDao.executeUpdateBySQL(sql, param);
	}

	@Override
	public int executeSql(String sql, Map<String, Object> param) {
		return baseDao.executeUpdateBySQL(sql, param);
	}

	@Override
	public int executeSql(String sql, Object... param) {
		return baseDao.executeUpdateBySQL(sql, param);
	}

	@Override
	public <T> List<T> findByJPA(String hqlString, Object... params) {
		return baseDao.findByJPA(hqlString, params);
	}

	@Override
	public <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException {
		return baseDao.findSingleByJPA(hqlString, params);
	}

	@Override
	public <T> List<T> findByHql(String hql, Map<String, Object> paramsMap) {
		return baseDao.queryObjectsByHql(hql, paramsMap);
	}

	@Override
	public List findByHql(String hql, Object... param) {
		return baseDao.queryObjectsByHql(hql, param);
	}

	@Override
	public List findBySql(String sql) {
		return baseDao.queryObjectsSql(sql);
	}

	@Override
	public <T> T findSingleResult(String hql, Object...values) {
		return (T) baseDao.findSingleResult(hql, values);
	}

	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value) throws HibernateException {

		return baseDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	@Override
	public <T> T get(Class<T> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}
	
	@Override
	public <T> Serializable save(T entity) {
		return baseDao.save(entity);
	}

	@Override
	public <T> void saveBatch(List<T> entities) {
		this.baseDao.addBatch(entities);
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		baseDao.saveOrUpdate(entity);
	}

	@Override
	public <T> void update(T pojo) {
		baseDao.update(pojo);
	}

	@Override
	public int updateBySql(String sql, Object... params) {
		return baseDao.executeUpdateBySQL(sql, params);
	}
	
	@Override
	public <T> List<T> search(Query query) {
		
		return baseDao.search(query);
	}

	@Override
	public <T> Page<T> search(Query query, Page<T> page) {
		
		return baseDao.search(query, page);
	}
	
	@Override
	public Page search(String hqlString, Page page, Map<String, Object> paramsMap){
		
		return baseDao.search(hqlString, page, paramsMap);
		
	}

	@Override
	public int count(Query query) {
		
		return baseDao.count(query);
	}

	@Override
	public int bulkUpdate(String queryString, Object... values) {
		
		return baseDao.bulkUpdate(queryString, values);
	}
	
	@Override
	public int updateBulk(String queryString, Object... values) {
		
		return baseDao.bulkUpdate(queryString, values);
	}

	@Override
	public int updateBulkByMap(String queryString, Map<String, Object> paramsMap){
		
		return baseDao.bulkUpdateByMap(queryString, paramsMap);
	}
}
