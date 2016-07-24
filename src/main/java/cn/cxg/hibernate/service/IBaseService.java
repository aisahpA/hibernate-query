package cn.cxg.hibernate.service;

import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service公用方法接口
 * 
 * @author chenxianguan
 *
 */
public interface IBaseService {

	/**
	 * 删除实体
	 * 
	 * @param entity 待删除实体
	 */
	<T> void delete(T entity);

	/**
	 * 删除实体集合
	 * 
	 * @param entities 待删除实体集合
	 */
	<T> void deleteAllEntities(Collection<T> entities);

	/**
	 * 根据主键删除实体
	 * 
	 * @param entityClass 实体类型
	 * @param id 主键id
	 */
	<T> void deleteEntityById(Class<T> entityClass, Serializable id);

	/**
	 * 执行SQL
	 * 
	 * @param sql sql语句
	 * @param param 按？顺序的参数值
	 * @return The number of entities updated or deleted.
	 */
	int executeSql(String sql, List<Object> param);

	/**
	 * 执行SQL 使用:name占位符
	 * 
	 * @param sql sql语句
	 * @param param name以及对于的参数值
	 * @return The number of entities updated or deleted.
	 */
	int executeSql(String sql, Map<String, Object> param);

	/**
	 * 执行SQL
	 * 
	 * @param sql sql语句
	 * @param param 按？顺序的参数值
	 * @return The number of entities updated or deleted.
	 */
	int executeSql(String sql, Object... param);

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
	 * 通过hql 查询语句查找对象
	 * 
	 * @param hql 待查询的HQL语句
	 * @param paramsMap 命名参数名称及对应的值
	 * @return 查询结果
	 */
	<T> List<T> findByHql(String hql, Map<String, Object> paramsMap);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param hql hql语句
	 * @param param 按？顺序的参数值
	 * @return 查询结果集合
	 */
	List findByHql(String hql, Object... param);

	/**
	 * 根据sql查找List
	 * 
	 * @param sql sql语句
	 * @return 查询结果集合
	 */
	List findBySql(String sql);

	/**
	 * 根据条件查询唯一对象
	 * 
	 * @param hql hql语句
	 * @return the single result or null
	 */
	<T> T findSingleResult(String hql, Object... values);

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 *
	 * @param entityClass 实体类型
	 * @param propertyName 字段名称
	 * @param value 字段值
	 * @return the single result or null
     * @throws HibernateException if there is more than one matching result
	 */
	<T> T findUniqueByProperty(Class<T> entityClass,
                               String propertyName, Object value) throws HibernateException;

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param entityClass 实体类型
	 * @param id 主键id
	 * @return the persistent instance, or null if not found
	 */
	<T> T get(Class<T> entityClass, Serializable id);

	/**
	 * 保存对象
	 *
	 * @param entity 实体
	 * @return the generated identifier
	 */
	<T> Serializable save(T entity);

	/**
	 * 批量保存对象
	 * 
	 * @param entities 多个实体
	 */
	<T> void saveBatch(List<T> entities);

	/**
	 * 保存或更新对象
	 * 
	 * @param entity 实体
	 */
	<T> void saveOrUpdate(T entity);

	/**
	 * 更新指定的实体
	 * 
	 * @param pojo 实体
	 */
	<T> void update(T pojo);

	/**
	 * 根据sql更新
	 * 
	 * @param sql 待执行的sql语句
	 * @param params 按sql语句中问号顺序添加的参数
	 * @return The number of entities updated or deleted.
	 */
	int updateBySql(String sql, Object... params);
	
	/**
	 * 直接query查询
	 * 
	 * @param query 查询条件
	 * @return 查询结果
	 */
	<T> List<T> search(Query query);
	
	/**
	 * 分页查询
	 * 
	 * @param query 查询条件
	 * @param page 分页信息
	 * @return 分页数据
	 */
	<T> Page<T> search(Query query, Page<T> page);
	
	/**
	 * 根据hql分页查询
	 * 
	 * @param hqlString The HQL query
	 * @param page 分页信息
	 * @param paramsMap 参数值的map
	 * @return 分页数据
	 */
	Page search(String hqlString, Page page, Map<String, Object> paramsMap);
	
	/**
	 * 查询记录数量
	 *
	 * @param query 查询条件
	 * @return 记录数量
	 * @since create chenxianguan 2015年11月22日 上午3:39:01
	 */
	int count(Query query);
	
	/**
	 * Update/delete all objects according to the given query, binding a number of
	 * values to "?" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return the number of instances updated/deleted
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#bulkUpdate(String, Object...)
	 * @see #updateBulk(String, Object...)
	 */
	int bulkUpdate(String queryString, Object... values);
	
	/**
	 * 同bulkUpdate方法，只是它有事务
	 * Update/delete all objects according to the given query, binding a number of
	 * values to "?" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return the number of instances updated/deleted
	 * @see org.springframework.orm.hibernate4.HibernateTemplate#bulkUpdate(String, Object...)
	 */
	int updateBulk(String queryString, Object... values);
	
	/**
	 * Update/delete all objects according to the given query, binding a number of
	 * values to ":" parameters in the query string.
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param paramsMap 字符串指定参数以及对应值集合
	 * @return the number of instances updated/deleted
	 * @since create chenxianguan 2016年1月14日 下午7:30:42
	 */
	int updateBulkByMap(String queryString, Map<String, Object> paramsMap);
	

}
