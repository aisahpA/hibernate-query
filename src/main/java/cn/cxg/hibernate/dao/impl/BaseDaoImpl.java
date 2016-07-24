package cn.cxg.hibernate.dao.impl;

import cn.cxg.hibernate.dao.IBaseDao;
import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import cn.cxg.hibernate.util.QueryHelper;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>dao的实现类，继承了spring的hibernate工具类
 * 使用方法：业务模块下的dao如果仅需要一些原生的方法，可以继承此类
 * </pre>
 * 
 * @author chenxianguan  2015年12月3日
 *
 */
public class BaseDaoImpl extends HibernateDaoSupport implements IBaseDao {

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	//-------------------------------------------------------------------------
	// Convenience methods for loading individual objects
	//-------------------------------------------------------------------------
	@Override
	public <T> T get(Class<T> entityClass, Serializable id) {
		return this.getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		return this.getHibernateTemplate().load(entityClass, id);
	}
	
	//-------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	//-------------------------------------------------------------------------
	@Override
	public Serializable save(Object entity) {
		return getHibernateTemplate().save(entity);
	}

	@Override
	public Object update(Object entity) {
		getHibernateTemplate().update(entity);		
		return entity;
	}
	
	@Override
	public Object saveOrUpdate(Object entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void delete(Object entity) {
		getHibernateTemplate().delete(entity);
	}
	
	@Override
	public void addBatch(List<?> entities){
		Session session = this.currentSession();
		int num = 0;
		for(Object entity : entities){
			session.save(entity);
			if(++num % 30 == 0){
				session.flush();  
				session.clear();
			}
		}
	}
	
	@Override
	public void addObjects(List<?> entities) {
		if ((entities == null) || (entities.isEmpty()))
			return;
		Session session = this.currentSession();
		for (Object t : entities) {
			session.save(t);
		}
		session.flush();
	}
	
	@Override
	public void updateObjects(List<?> entities) {
		if ((entities == null) || (entities.isEmpty()))
			return;
		for (Object entity : entities) {
			getHibernateTemplate().saveOrUpdate(entity);
		}
	}
	
	@Override
	public void deleteObjects(List<?> entities) {
		if ((entities == null) || (entities.isEmpty()))
			return;
		Session session = this.currentSession();
		for (Object object : entities) {
			session.delete(object);
		}
		session.flush();
	}
	
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	//-------------------------------------------------------------------------
	/**
	 * 设置Query的参数
	 * @param query org.hibernate.Query
	 * @param params 待设置的参数
	 * @since chenxianguan 2015年12月3日下午4:38:49
	 */
	protected void setParams(org.hibernate.Query query, Object... params){
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
	}
    /**
     * 设置Query的参数：JPA模式，从1开始
     *
     * @param query org.hibernate.Query
     * @param params 待设置的参数
     * @since chenxianguan 2016年6月22日
     */
    protected void setParamsJPA(org.hibernate.Query query, Object... params){
        if (params != null) {
            Map<String, Object> paramsMap = new HashMap<>(params.length);
            for (int i = 0; i < params.length; i++) {
				paramsMap.put(String.valueOf(i+1), params[i]);
            }
            this.setParams(query, paramsMap);
        }
    }
	/**
	 * 设置Query的参数
	 * @param query org.hibernate.Query
	 * @param paramsMap 待设置的参数对
	 * @since chenxianguan 2015年12月3日下午4:38:52
	 */
	protected void setParams(org.hibernate.Query query, Map<String, Object> paramsMap){
		if (paramsMap != null) {
            String key;
			Object value;
			for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                key = entry.getKey();
				value = entry.getValue();
				if(value == null){
					continue;
				}
				if(value instanceof Collection){
					query.setParameterList(key, (Collection)value);
				} else if(value.getClass().isArray()){
					query.setParameterList(key, (Object[])value);
				} else {
					query.setParameter(key, value);
				}
			}			
		}
	}
	
	@Override
	public Object findSingleResult(String hqlString, Object...params) throws HibernateException {
		List<?> list = getHibernateTemplate().find(hqlString, params);
        return dealWithSingle(list);
	}
	
	@Override
	public List<?> find(String hql, Object...params){
		return this.getHibernateTemplate().find(hql, params);
	}
	
	@Override
	public List<?> findByNamedParam(String hqlString, String paramName, Object value){
		return this.getHibernateTemplate().findByNamedParam(hqlString, paramName, value);
	}
	
	@Override
	public List<?> findByNamedParam(String hqlString, String[] paramNames, Object[] values) {
		return this.getHibernateTemplate().findByNamedParam(hqlString, paramNames, values);
	}
	
	@Override
	public List<?> findByNamedParam(String hqlString, Map<String, Object> paramsMap){
		String[] paramNames = new String[paramsMap.size()];
		Object[] values = new Object[paramsMap.size()];
		int i=0;
		for(Map.Entry<String, Object> param : paramsMap.entrySet()){
			paramNames[i] = param.getKey();
			values[i] = param.getValue();
			i++;
		}
		return this.getHibernateTemplate().findByNamedParam(hqlString, paramNames, values);
	}

    @Override
    public <T> List<T> findByJPA(String hqlString, Object... params) {
        org.hibernate.Query query = this.currentSession().createQuery(hqlString);
        this.setParamsJPA(query, params);
        return query.list();
    }

    @Override
    public <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException {
        org.hibernate.Query query = this.currentSession().createQuery(hqlString);
        this.setParamsJPA(query, params);
        List<T> list = query.list();
        return dealWithSingle(list);
    }

    /**
     * 处理单个返回
     *
     * @param list 查询结果
     * @param <T> 结果对象类型
     * @return 查询的唯一一个结果
     * @throws HibernateException 结果数量大于1的时候
     * @since chenxianugan 2016年7月6日
     */
    private <T> T dealWithSingle(List<T> list) throws HibernateException {
        if(list == null || list.isEmpty()){
            return null;
        } else if(list.size()>1){
            throw new HibernateException("结果集数量: " + list.size() + "大于1");
        } else {
            return list.get(0);
        }
    }

    @Override
	public <T> List<T> queryObjectsByHql(String hqlString){
		return this.queryObjectsByHql(hqlString, new Object[0]);
	}
	
	@Override
	public <T> List<T> queryObjectsByHql(String hqlString, List<Object> params) {
		return this.queryObjectsByHql(hqlString, params.toArray(new Object[params.size()]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryObjectsByHql(String hqlString, Object[] params) {
		org.hibernate.Query query = this.currentSession().createQuery(hqlString);
		this.setParams(query, params);
		return query.list();
	}
	
	@Override
	public <T> List<T> queryObjectsByHql(String hqlString, Map<String, Object> paramsMap) {
		return queryObjectsByHql(hqlString, paramsMap, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryObjectsByHql(String hqlString, Map<String, Object> paramsMap,
			Integer firstResult, Integer maxResults) {
		org.hibernate.Query query = this.currentSession().createQuery(hqlString);
		this.setParams(query, paramsMap);
		
		if(firstResult != null){
			query.setFirstResult(firstResult);
		}
		if(maxResults != null){
			query.setMaxResults(maxResults);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page search(String hqlString, Page page, Map<String, Object> paramsMap){
		
		org.hibernate.Query query = this.currentSession().createQuery(hqlString);
		this.setParams(query, paramsMap);
		
		page.setTotalCount(query.list().size());
		
		query.setFirstResult(page.getStartIndex());
		query.setMaxResults(page.getPageSize());
		
		page.setResults(query.list());
		
		return page;
		
	}
	
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	//-------------------------------------------------------------------------	
	@Override
	public int count(Query query) {
		
		DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
		
		return this.count(detachedCriteria);
	}
	
	@Override
	public int count(DetachedCriteria detachedCriteria) {
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(this.currentSession());
		
		return this.count(criteria);
		
	}
	
	@Override
	public int count(Criteria criteria){
		criteria.setProjection(Projections.rowCount());
		criteria.setFirstResult(0);
		
		int num = Integer.valueOf(criteria.uniqueResult().toString());
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		
		return num;
	}

	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName,
			Object value) throws HibernateException{
		Assert.hasText(propertyName);
		
		Criteria criteria = this.currentSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(propertyName, value));
		
		return  (T) criteria.uniqueResult();
	}
	
	
	
	@Override
	public List<?> search(DetachedCriteria detachedCriteria) {
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public  List<?> search(DetachedCriteria detachedCriteria, int firstResult,
			int maxResults) {
		return getHibernateTemplate().findByCriteria(detachedCriteria, firstResult,
				maxResults);
	}
	
	@Override
	public Page search(DetachedCriteria detachedCriteria, Page page){
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(this.currentSession());
		
		if(page == null){
			page = Page.checkValid(page);
		} else {
			criteria.setFirstResult(page.getStartIndex());
			criteria.setMaxResults(page.getPageSize());
		}

		List list = criteria.list();
		
		page.setResults(list);
			
		page.setTotalCount(this.count(criteria));
				
		return page;
	}
	
	@Override
	public <T> List<T> search(Query query) {
		
		DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
		
		return (List<T>) this.search(detachedCriteria);
	}

	@Override
	public Page search(Query query, Page page) {
		
		DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
		
		return this.search(detachedCriteria, page);
	}
	
	
	
	//-------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	//-------------------------------------------------------------------------
	@Override
	public int bulkUpdate(String queryString, Object... values) {
		return getHibernateTemplate().bulkUpdate(queryString, values);
	}

    @Override
    public int bulkUpdateByJPA(String queryString, Object... values) {
        org.hibernate.Query query = this.currentSession().createQuery(queryString);
        this.setParamsJPA(query, values);

        return query.executeUpdate();
    }

    @Override
	public int bulkUpdateByMap(String queryString, Map<String, Object> paramsMap){
		org.hibernate.Query query = this.currentSession().createQuery(queryString);
		this.setParams(query, paramsMap);
		
		return query.executeUpdate();
	}
	
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for SQL strings
	//-------------------------------------------------------------------------		
	@Override
	public void commitSQL(String sql) {
		sql = sql.toLowerCase();
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	@Override
	public int executeUpdateBySQL(String sql, Object... params) {
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		this.setParams(query, params);
		
		return query.executeUpdate();
	}
	
	@Override
	public int executeUpdateBySQL(String sql, List<Object> params) {
		
		return executeUpdateBySQL(sql, params.toArray(new Object[params.size()]));
	}

	@Override
	public int executeUpdateBySQL(String sql, Map<String, Object> paramsMap) {
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		this.setParams(query, paramsMap);
		
		return query.executeUpdate();
	}
	
	
	@Override
	public List queryObjectsSql(String sql) {
		
		return this.queryObjectsBySql(null, sql, new Object[0]);
	}
	
	@Override
	public List queryObjectsSql(String sql, List<Object> params) {
		
		return this.queryObjectsBySql(null, sql, params.toArray(new Object[params.size()]));
	}
	
	@Override
	public List queryObjectsSql(String sql, Object[] params) {
		
		return this.queryObjectsBySql(null, sql, params);
	}

	@Override
	public List queryObjectsSql(String sql, Map<String, Object> paramsMap) {
		
		return this.queryObjectsBySql(null, sql, paramsMap, null, null);
	}
	
	@Override
	public List queryObjectsBySql(String sql, Map<String, Object> paramsMap,
			Integer firstResult, Integer maxResults) {
		
		return this.queryObjectsBySql(null, sql, paramsMap, firstResult, maxResults);
	}
	
	@Override
	public List queryObjectsBySql(Class clazz, String sql) {
		
		return queryObjectsBySql(clazz, sql, new Object[0]);
	}

	@Override
	public List queryObjectsBySql(Class clazz, String sql, List<Object> params) {
		
		return queryObjectsBySql(clazz, sql, params.toArray(new Object[params.size()]));
	}

	@Override
	public List queryObjectsBySql(Class clazz, String sql, Object[] params) {
		
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		if (clazz != null) {
			query.addEntity(clazz);
		}
		this.setParams(query, params);
		
		return query.list();
	}
	
	@Override
	public List queryObjectsBySql(Class clazz, String sql, Map<String, Object> paramsMap) {
		
		return this.queryObjectsBySql(clazz, sql, paramsMap, null, null);
	}
	
	@Override
	public List queryObjectsBySql(Class clazz, String sql,
			Map<String, Object> paramsMap, Integer firstResult, Integer maxResults) {
		
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		if (clazz != null) {
			query.addEntity(clazz);
		}
		this.setParams(query, paramsMap);
		
		if(firstResult != null){
			query.setFirstResult(firstResult);
		}
		if(maxResults != null){
			query.setMaxResults(maxResults);
		}
		
		return query.list();
	}
	
}
