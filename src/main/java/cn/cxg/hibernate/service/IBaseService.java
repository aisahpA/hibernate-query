package cn.cxg.hibernate.service;

import cn.cxg.hibernate.domain.IDomainObject;
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
 * @author chenxianguan 2016/7/24
 */
public interface IBaseService {

    //-------------------------------------------------------------------------
    // 使用实体进行操作
    //-------------------------------------------------------------------------

    /**
     * 根据实体名称和主键获取实体
     *
     * @param entityClass 实体类型
     * @param id          主键id
     * @return the persistent instance, or null if not found
     */
    <T> T get(Class<T> entityClass, Serializable id);

    /**
     * 保存对象
     *
     * @param entity 实体
     * @return the generated identifier
     */
    Serializable save(IDomainObject entity);

    /**
     * 更新指定的实体
     *
     * @param entity 实体
     */
    void update(IDomainObject entity);

    /**
     * 删除一个实体
     *
     * @param entity 待删除实体
     */
    void delete(IDomainObject entity);

    /**
     * 根据主键删除一个实体
     *
     * @param entityClass 实体类型
     * @param id          主键id
     */
    void delete(Class<? extends IDomainObject> entityClass, Serializable id);

    /**
     * 删除多个实体
     *
     * @param entities 待删除实体集合
     */
    void deleteAll(Collection<? extends IDomainObject> entities);


    //-------------------------------------------------------------------------
    // 使用HQL进行操作
    //-------------------------------------------------------------------------

    /**
     * 查询一个结果的 HQL query， binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params    按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T>       result type
     * @return 唯一的一个结果 or null
     * @throws HibernateException 当查询到多个结果时抛出异常
     */
    <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException;

    /**
     * 查询多个结果的 HQL query, binding a number of values to "?1" parameters
     *
     * @param hqlString The HQL query, ?num based from 1
     * @param params    按HQL中?num值对应的各参数值，即第一个参数对应?1，第二个对应?2，以此类推
     * @param <T>       result type
     * @return 查询结果，list不会为空
     */
    <T> List<T> findByJPA(String hqlString, Object... params);

    /**
     * 通过hql 查询语句查找对象
     *
     * @param hqlString 待查询的HQL语句
     * @param paramsMap 命名参数名称及对应的值
     * @return 查询结果，list不会为空
     */
    <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap);

    /**
     * 根据hql分页查询
     *
     * @param hqlString The HQL query
     * @param paramsMap 参数值的map
     * @param page      分页信息
     * @return 分页数据
     */
    Page search(String hqlString, Map<String, Object> paramsMap, Page page);


    //-------------------------------------------------------------------------
    // 使用Query进行操作
    //-------------------------------------------------------------------------

    /**
     * 查询记录数量
     *
     * @param query 查询条件
     * @return 记录数量
     * @since create chenxianguan 2015年11月22日 上午3:39:01
     */
    int count(Query query);

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
     * @param page  分页信息
     * @return 分页数据
     */
    <T> Page<T> search(Query query, Page<T> page);


    //-------------------------------------------------------------------------
    // Convenience query methods for iteration and bulk updates/deletes
    //-------------------------------------------------------------------------

    /**
     * 同bulkUpdate方法，只是它有事务
     * Update/delete all objects according to the given query, binding a number of
     * values to "?" parameters in the query string.
     *
     * @param hqlString an update/delete query expressed in Hibernate's query language
     * @param values    the values of the parameters
     * @return 更新或删除的记录数量
     */
    int updateBulk(String hqlString, Object... values);

}
