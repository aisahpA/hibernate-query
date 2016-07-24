package cn.cxg.hibernate.dao;

import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 提供Dao基本操作方法
 * 
 * @author chenxianguan  2015年12月3日
 *
 */
public interface IBaseDao {
	
	//-------------------------------------------------------------------------
	// Convenience methods for loading individual objects
	//-------------------------------------------------------------------------
	/**
	 * 通过主键查询对象
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @return 查询成功，返回对象，否则返回null
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#get(Class, Serializable)
	 */
	<T> T get(Class<T> entityClass, Serializable id);
	
	/**
	 * 通过主键懒加载对象
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @return 查询成功，返回对象，否则抛出异常
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#load(Class, Serializable)
	 */
	<T> T load(Class<T> entityClass, Serializable id);
	
	
	//-------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	//-------------------------------------------------------------------------
	/**
	 * 保存对象
	 * @param entity  the transient instance to persist
	 * @return 返回保存对象的主键
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#save(Object)
	 */
	Serializable save(Object entity);
	
	/**
	 * 更新对象
	 * @param entity  the persistent instance to update
	 * @return 传入的参数entity
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#update(Object)
	 */
	Object update(Object entity);
	
	/**
	 * 保存或更新对象
	 * @param entity  the persistent instance to save or update (to be associated with the Hibernate Session)
	 * @return 传入的参数entity
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#saveOrUpdate(Object)
	 */
	Object saveOrUpdate(Object entity);
	
	/**
	 * 通过对象删除实体，实体要求为持久态或者瞬时态加入identifier
	 * @param entity  the persistent instance to delete
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#delete(Object)
	 */
	void delete(Object entity);
	
	/**
	 * 批量保存 
	 * <p>内部循环调用session.save(entity);
	 * <p>此方法会调用session.flush()和 session.clear();
	 * @param entities 待保存对象集合
	 */
	void addBatch(List<?> entities);
	
	/**
	 * 保存多个对象
	 * @param entities
	 */
	void addObjects(List<?> entities);
	
	/**
	 * 添加或更新多个对象
	 * @param entities
	 */
	void updateObjects(List<?> entities);
	
	/**
	 * 删除多个对象
	 * @param entities
	 */
	void deleteObjects(List<?> entities);
	
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	//-------------------------------------------------------------------------
	/**
	 * 根据hql查询唯一对象
	 * <p>如果没有找到对象，返回null
	 * <p>如果查询出多个对象，则抛出HibernateException异常
	 * @param hqlString The HQL query
	 * @param params 按HQL中问号顺序对应的各参数值
	 * @return the single result or null
	 * @throws HibernateException if there is more than one matching result
	 */
	Object findSingleResult(String hqlString, Object... params) throws HibernateException;
	
	/**
	 * Execute an HQL query, binding a number of values to "?" parameters
	 * in the query string.
	 * @param hqlString a query expressed in Hibernate's query language
	 * @param params the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @see org.hibernate.Session#createQuery
	 */
	List<?> find(String hqlString, Object... params);
	
	/**
	 * Execute an HQL query, binding one value to a ":" named parameter
	 * in the query string.
	 * @param hqlString a query expressed in Hibernate's query language
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @return a {@link List} containing the results of the query execution
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	List<?> findByNamedParam(String hqlString, String paramName, Object value);

	/**
	 * Execute an HQL query, binding a number of values to ":" named
	 * parameters in the query string.
	 * @param hqlString a query expressed in Hibernate's query language
	 * @param paramNames the names of the parameters
	 * @param values the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	List<?> findByNamedParam(String hqlString, String[] paramNames, Object[] values);
	
	/**
	 * Execute an HQL query, binding a number of values to ":" named
	 * parameters in the query string.
	 * @param hqlString a query expressed in Hibernate's query language
	 * @param paramsMap the {@code <name, value>}} of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	List<?> findByNamedParam(String hqlString, Map<String, Object> paramsMap);

    /**
     * Execute an HQL query, binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params 按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T> result type
     * @return a {@link List} containing the results of the query execution
     */
    <T> List<T> findByJPA(String hqlString, Object... params);

    /**
     * Execute an HQL query, binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params 按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T> result type
     * @return the single result or null
     * @throws HibernateException if there is more than one matching result
     */
    <T> T findSingleByJPA(String hqlString, Object... params)  throws HibernateException;


    /**
     * Execute an HQL query, binding a number of values to "?" parameters
     * @param hqlString The HQL query
     * @return a {@link List} containing the results of the query execution
     * @see #find(String, Object...)
     */
	@Deprecated
	<T> List<T> queryObjectsByHql(String hqlString);
	
	/**
	 * Execute an HQL query, binding a number of values to "?" parameters
	 * @param hqlString The HQL query
	 * @param params
	 * @return a {@link List} containing the results of the query execution
	 */
	<T> List<T> queryObjectsByHql(String hqlString, List<Object> params);
	
	/**
	 * Execute an HQL query, binding a number of values to "?" parameters
	 * @param hqlString The HQL query
	 * @param params 按HQL中问号顺序对应的各参数值
	 * @return a {@link List} containing the results of the query execution
	 * @see #find(String, Object...)
	 */
	<T> List<T> queryObjectsByHql(String hqlString, Object[] params);
	
	/**
	 * Execute an HQL query, binding a number of values to ":" named
	 * @param hqlString The HQL query
	 * @param paramsMap 命名参数名称及对应的值
	 * @return a {@link List} containing the results of the query execution
	 * @see #findByNamedParam(String, String[], Object[])
	 */
	<T> List<T> queryObjectsByHql(String hqlString, Map<String, Object> paramsMap);

	/** 
	 * Execute an HQL query, binding a number of values to ":" named
	 * @param hqlString The HQL query
	 * @param paramsMap 命名参数名称及对应的值
	 * @param firstResult  the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults  the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing the results of the query execution
	 */
	<T> List<T> queryObjectsByHql(String hqlString, Map<String, Object> paramsMap,
                                  Integer firstResult, Integer maxResults);

	/**
	 * 根据hql分页查询
	 * 
	 * @param hqlString The HQL query
	 * @param page 分页信息
	 * @param paramsMap 参数值的map
	 * @return
	 */
	Page search(String hqlString, Page page, Map<String, Object> paramsMap);
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	//-------------------------------------------------------------------------
	/**
	 * 查询记录数量
	 * @param query
	 * @return
	 */
	int count(Query query);
	
	/**
	 * 查询记录数量
	 * @param detachedCriteria
	 * @return
	 */
	int count(DetachedCriteria detachedCriteria);
	
	/**
	 * 查询记录数量
	 * @param criteria
	 * @return
	 */
	int count(Criteria criteria);
	
	/**
	 * 根据实体的属性查询唯一对象
	 * 
	 * @param entityClass 查询对象
	 * @param propertyName 查询条件属性名称
	 * @param value 查询条件属性值
	 * @return the single result or null
	 * @throws HibernateException if there is more than one matching result
	 */
	<T> T findUniqueByProperty(Class<T> entityClass,
                               String propertyName, Object value) throws HibernateException;

	
	/**
	 * Execute a query based on a given Hibernate criteria object.
	 * @param detachedCriteria the detached Hibernate criteria object.
	 * <b>Note: Do not reuse criteria objects! They need to recreated per execution,
	 * due to the suboptimal design of Hibernate's criteria facility.</b>
	 * @return a {@link List} containing 0 or more persistent instances
	 * @see DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
	 */
	List<?> search(DetachedCriteria detachedCriteria);

	/**
	 * Execute a query based on the given Hibernate criteria object.
	 * @param detachedCriteria the detached Hibernate criteria object.
	 * <b>Note: Do not reuse criteria objects! They need to recreated per execution,
	 * due to the suboptimal design of Hibernate's criteria facility.</b>
	 * @param firstResult the index of the first result object to be retrieved
	 * (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve
	 * (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @see DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
	 * @see Criteria#setFirstResult(int)
	 * @see Criteria#setMaxResults(int)
	 */
	List<?> search(DetachedCriteria detachedCriteria, int firstResult, int maxResults);

	/**
	 * 分页查询
	 * <p>当page为null时，查询所有数据
	 * @param detachedCriteria
	 * @param page
	 * @return
	 */
	Page search(DetachedCriteria detachedCriteria, Page page);
	
	/**
	 * 普通的查询
	 * 
	 * @param query 查询条件
	 * @return
	 */
	<T> List<T> search(Query query);

	/**
	 * 分页查询
	 * <p>当page为null时，查询所有数据
	 * 
	 * @param query
	 * @param page
	 * @return
	 */
	Page search(Query query, Page page);
	
	
	
	//-------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	//-------------------------------------------------------------------------
	/**
	 * Update/delete all objects according to the given query, binding a number of
	 * values to "?" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return the number of instances updated/deleted
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#bulkUpdate(String, Object...)
	 */
	int bulkUpdate(String queryString, Object... values);

	/**
	 * Update/delete all objects according to the given query, binding a number of
	 * values to "?num" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language， ?num based from 1
	 * @param values the values of the parameters 第一个参数对应?1，第二个对应?2，以此类推
	 * @return the number of instances updated/deleted
	 */
	int bulkUpdateByJPA(String queryString, Object... values);
	
	/**
	 * Update/delete all objects according to the given query, binding a number of
	 * values to ":" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param paramsMap 字符串指定参数以及对应值集合
	 * @return the number of instances updated/deleted
	 * @since create chenxianguan 2016年1月14日 下午7:30:42
	 */
	int bulkUpdateByMap(String queryString, Map<String, Object> paramsMap);
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for SQL strings
	//-------------------------------------------------------------------------
	/**
	 * 执行sql语句，此方法已废弃，请用 {@link #executeUpdateBySQL(String, Object...)}代替
	 * @param sql The SQL query
	 * @see #executeUpdateBySQL(String, Object...)
	 */
	@Deprecated
	void commitSQL(String sql);
	
	/**
	 * 执行带参数的sql(update or delete)
	 * 
	 * @param sql 待执行的sql语句
	 * @param params 按sql语句中问号顺序添加的参数
	 * @return The number of entities updated or deleted.
	 * @see org.hibernate.Query#executeUpdate()
	 * @see org.hibernate.Query#setParameter(int, Object)
	 */
	int executeUpdateBySQL(String sql, Object... params);

	/**
	 * 执行带参数的sql(update or delete)
	 * 
	 * @param sql 待执行的sql语句
	 * @param params 按sql语句中问号顺序添加的参数
	 * @return The number of entities updated or deleted.
	 * @see org.hibernate.Query#executeUpdate()
	 * @see org.hibernate.Query#setParameter(int, Object)
	 */
	int executeUpdateBySQL(String sql, List<Object> params);

	/**
	 * 执行带参数的sql(update or delete)
	 * 
	 * @param sql 待执行的sql语句
	 * @param paramsMap sql语句中命名参数及其对应的值
	 * @return The number of entities updated or deleted.
	 * @see org.hibernate.Query#executeUpdate()
	 * @see org.hibernate.Query#setParameter(String, Object)
	 */
	int executeUpdateBySQL(String sql, Map<String, Object> paramsMap);
	
	
	/**
	 * 根据sql查询
	 * @param sql sql查询语句
	 * @return 查询结果
	 * create chenxianguan 2015年12月3日下午8:28:33
	 */
	List queryObjectsSql(String sql);
	
	List queryObjectsSql(String sql, Object[] params);
	
	List queryObjectsSql(String sql, List<Object> params);
	
	List queryObjectsSql(String sql, Map<String, Object> paramsMap);
	
	List queryObjectsBySql(String sql, Map<String, Object> paramsMap, Integer firstResult, Integer maxResults);
	
	/**
	 * 根据sql查询，并组装为实体对象返回
	 * <p>此方法好像只支持select * 查询
	 * @param clazz 返回的实体对象类型
	 * @param sql sql查询语句
	 * @return 实体对象集合
	 * create chenxianguan 2015年12月3日下午8:20:20
	 */
	List queryObjectsBySql(Class clazz, String sql);
	
	List queryObjectsBySql(Class clazz, String sql, List<Object> params);
	
	List queryObjectsBySql(Class clazz, String sql, Object[] params);
	
	List queryObjectsBySql(Class clazz, String sql, Map<String, Object> paramsMap);
	
	List queryObjectsBySql(Class clazz, String sql, Map<String, Object> paramsMap, Integer firstResult, Integer maxResults);
	

}
