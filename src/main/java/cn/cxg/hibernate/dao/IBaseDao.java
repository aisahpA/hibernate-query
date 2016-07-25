package cn.cxg.hibernate.dao;

import cn.cxg.hibernate.domain.IDomainObject;
import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 提供Dao基本操作方法
 *
 * @author chenxianguan  2016/7/24
 */
public interface IBaseDao {

    //-------------------------------------------------------------------------
    // Convenience methods for loading individual objects
    //-------------------------------------------------------------------------

    /**
     * 通过主键查询对象
     *
     * @param entityClass a persistent class
     * @param id          the identifier of the persistent instance
     * @return 查询成功，返回对象，否则返回null
     */
    <T> T get(Class<T> entityClass, Serializable id);

    /**
     * 通过主键懒加载对象
     *
     * @param entityClass a persistent class
     * @param id          the identifier of the persistent instance
     * @return 查询成功，返回对象，否则抛出异常
     */
    <T> T load(Class<T> entityClass, Serializable id);

    /**
     * Re-read the state of the given persistent instance.
     *
     * @param entity the persistent instance to re-read
     * @see org.hibernate.Session#refresh(Object)
     */
    void refresh(IDomainObject entity);


    //-------------------------------------------------------------------------
    // Convenience methods for storing individual objects
    //-------------------------------------------------------------------------

    /**
     * 保存对象
     *
     * @param entity the transient instance to persist
     * @return 返回保存对象的主键
     */
    Serializable save(IDomainObject entity);

    /**
     * 更新对象
     *
     * @param entity the persistent instance to update
     */
    void update(IDomainObject entity);

    /**
     * 通过对象删除实体，实体要求为持久态或者瞬时态加入identifier
     *
     * @param entity the persistent instance to delete
     */
    void delete(IDomainObject entity);

    /**
     * 删除多个对象
     *
     * @param entities 待删除的多个实体对象
     */
    void deleteAll(Collection<? extends IDomainObject> entities);


    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------

    /**
     * Execute an HQL query, binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params    按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T>       result type
     * @return the single result or null
     * @throws HibernateException if there is more than one matching result
     */
    <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException;

    /**
     * Execute an HQL query, binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params    按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T>       result type
     * @return a {@link List} containing the results of the query execution
     */
    <T> List<T> findByJPA(String hqlString, Object... params);

    /**
     * Execute an HQL query, binding a number of values to ":" named
     *
     * @param hqlString The HQL query
     * @param paramsMap 命名参数名称及对应的值
     * @return a {@link List} containing the results of the query execution
     */
    <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap);

    /**
     * Execute an HQL query, binding a number of values to ":" named
     *
     * @param hqlString   The HQL query
     * @param paramsMap   命名参数名称及对应的值
     * @param firstResult the index of the first result object to be retrieved (numbered from 0)
     * @param maxResults  the maximum number of result objects to retrieve (or <=0 for no limit)
     * @return a {@link List} containing the results of the query execution
     */
    <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap, int firstResult, int maxResults);


    //-------------------------------------------------------------------------
    // Convenience finder methods for detached criteria
    //-------------------------------------------------------------------------

    /**
     * 查询记录数量
     *
     * @param query Query条件
     * @return 记录数量
     */
    int count(Query query);

    /**
     * 普通的查询
     *
     * @param query 查询条件
     * @return 查询结果
     */
    <T> List<T> search(Query query);

    /**
     * 分页查询
     * <p>当page为null时，查询所有数据
     *
     * @param query 查询条件
     * @param page  分页信息
     * @return 分页数据
     */
    <T> Page<T> search(Query query, Page<T> page);


    //-------------------------------------------------------------------------
    // Convenience query methods for iteration and bulk updates/deletes
    //-------------------------------------------------------------------------

    /**
     * Update/delete all objects according to the given query, binding a number of
     * values to "?" parameters in the query string.
     *
     * @param queryString an update/delete query expressed in Hibernate's query language
     * @param values      the values of the parameters
     * @return the number of instances updated/deleted
     */
    int bulkUpdate(String queryString, Object... values);

    //-------------------------------------------------------------------------
    // Convenience finder methods for SQL strings
    //-------------------------------------------------------------------------

    /**
     * 执行带参数的sql(update or delete)
     *
     * @param sql    待执行的sql语句
     * @param params 按sql语句中问号顺序添加的参数
     * @return The number of entities updated or deleted.
     * @see org.hibernate.Query#executeUpdate()
     * @see org.hibernate.Query#setParameter(int, Object)
     */
    int executeUpdateBySql(String sql, Object... params);

    /**
     * 执行带参数的sql(update or delete)
     *
     * @param sql       待执行的sql语句
     * @param paramsMap sql语句中命名参数及其对应的值
     * @return The number of entities updated or deleted.
     * @see org.hibernate.Query#executeUpdate()
     * @see org.hibernate.Query#setParameter(String, Object)
     */
    int executeUpdateBySql(String sql, Map<String, Object> paramsMap);


    /**
     * 根据sql查询，binding a number of values to "?" parameters
     *
     * @param sql sql查询语句
     * @param params 按SQL中?值对应的各参数值
     * @return 查询结果
     */
    <T> List<T> findBySql(String sql, Object... params);

    <T> List<T> findBySql(String sql, Map<String, Object> paramsMap);

    <T> List<T> findBySql(String sql, Map<String, Object> paramsMap, int firstResult, int maxResults);

    /**
     * 根据sql查询，并组装为实体对象返回，binding a number of values to "?" parameters
     * <p>此方法好像只支持select * 查询
     *
     * @param clazz 返回的实体对象类型
     * @param sql   sql查询语句
     * @param params 按SQL中?值对应的各参数值
     * @return 实体对象集合
     */
    <T> List<T> findBySql(Class<T> clazz, String sql, Object... params);

    <T> List<T> findBySql(Class<T> clazz, String sql, Map<String, Object> paramsMap);

    <T> List<T> findBySql(Class<T> clazz, String sql, Map<String, Object> paramsMap, int firstResult, int maxResults);


}
