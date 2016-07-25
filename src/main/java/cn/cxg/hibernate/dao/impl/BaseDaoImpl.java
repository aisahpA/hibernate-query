package cn.cxg.hibernate.dao.impl;

import cn.cxg.hibernate.dao.IBaseDao;
import cn.cxg.hibernate.domain.IDomainObject;
import cn.cxg.hibernate.query.NamedParam;
import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import cn.cxg.hibernate.util.QueryHelper;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * <pre>dao的实现类，继承了spring的hibernate工具类
 * 使用方法：业务模块下的dao如果仅需要一些原生的方法，可以继承此类
 * </pre>
 *
 * @author chenxianguan  2015年12月3日
 */
public class BaseDaoImpl extends HibernateDaoSupport implements IBaseDao {

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    //-------------------------------------------------------------------------
    // Convenience methods for loading individual objects
    //-------------------------------------------------------------------------
    @Override
    public <T> T get(Class<T> entityClass, Serializable id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public <T> T load(Class<T> entityClass, Serializable id) {
        return getHibernateTemplate().load(entityClass, id);
    }

    @Override
    public void refresh(final IDomainObject entity) {
        getHibernateTemplate().refresh(entity);
    }

    //-------------------------------------------------------------------------
    // Convenience methods for storing individual objects
    //-------------------------------------------------------------------------
    @Override
    public Serializable save(IDomainObject entity) {
        return getHibernateTemplate().save(entity);
    }

    @Override
    public void update(IDomainObject entity) {
        getHibernateTemplate().update(entity);
    }

    @Override
    public void delete(IDomainObject entity) {
        getHibernateTemplate().delete(entity);
    }

    @Override
    public void deleteAll(Collection<? extends IDomainObject> entities) {
        getHibernateTemplate().deleteAll(entities);
    }


    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------

    /**
     * 使用:命名参数查询
     * @param hqlString hql查询语句
     * @param queryParam 命名参数和值
     * @param <T> 泛型
     * @return 查询结果
     */
    private <T> List<T> findByHqlNamedParam(String hqlString, NamedParam queryParam) {
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) this.getHibernateTemplate().findByNamedParam(hqlString,
                queryParam.getParamNamesArr(), queryParam.getValuesArr());
        return list;
    }

    @Override
    public <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException {
        return dealWithSingle(findByJPA(hqlString, params));
    }

    @Override
    public <T> List<T> findByJPA(String hqlString, Object... params) {
        return findByHqlNamedParam(hqlString, new NamedParam(params));
    }

    @Override
    public <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap) {
        return findByHqlNamedParam(hqlString, new NamedParam(paramsMap));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap, int firstResult, int maxResults) {
        org.hibernate.Query query = this.currentSession().createQuery(hqlString);
        this.setParams(query, paramsMap);
        this.setFirstAndMaxResult(query, firstResult, maxResults);
        return query.list();
    }


    //-------------------------------------------------------------------------
    // Convenience finder methods for detached criteria
    //-------------------------------------------------------------------------
    @Override
    public int count(Query query) {
        DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
        return this.count(detachedCriteria);
    }

    protected int count(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(this.currentSession());
        return this.count(criteria);
    }

    protected int count(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        criteria.setFirstResult(0);
        int num = Integer.valueOf(criteria.uniqueResult().toString());

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);
        return num;
    }

    @Override
    public <T> List<T> search(Query query) {
        DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
        @SuppressWarnings("unchecked")
        List<T> results = (List<T>) getHibernateTemplate().findByCriteria(detachedCriteria);
        return results;
    }

    @Override
    public <T> Page<T> search(Query query, Page<T> page) {
        DetachedCriteria detachedCriteria = QueryHelper.pretreatmentQuery(query);
        return this.search(detachedCriteria, page);
    }

    @SuppressWarnings("unchecked")
    protected <T> Page<T> search(DetachedCriteria detachedCriteria, Page<T> page) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(this.currentSession());
        criteria.setFirstResult(page.getStartIndex());
        criteria.setMaxResults(page.getPageSize());

        page.setResults(criteria.list());
        page.setTotalCount(this.count(criteria));
        return page;
    }


    //-------------------------------------------------------------------------
    // Convenience query methods for iteration and bulk updates/deletes
    //-------------------------------------------------------------------------
    @Override
    public int bulkUpdate(String queryString, Object... values) {
        return getHibernateTemplate().bulkUpdate(queryString, values);
    }

    //-------------------------------------------------------------------------
    // 公用方法
    //-------------------------------------------------------------------------

    /**
     * 设置分页起始值和每页最大数量
     * @param query 查询
     * @param firstResult 起始值
     * @param maxResults 每页最大值
     */
    protected void setFirstAndMaxResult(org.hibernate.Query query, int firstResult, int maxResults) {
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
    }

    /**
     * 设置Query的参数
     *
     * @param query  org.hibernate.Query
     * @param params 待设置的参数
     */
    protected void setParams(org.hibernate.Query query, Object... params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
    }

    /**
     * 设置Query的参数
     *
     * @param query     org.hibernate.Query
     * @param paramsMap 待设置的参数对
     */
    protected void setParams(org.hibernate.Query query, Map<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof Collection) {
                query.setParameterList(key, (Collection) value);
            } else if (value.getClass().isArray()) {
                query.setParameterList(key, (Object[]) value);
            } else {
                query.setParameter(key, value);
            }
        }
    }

    /**
     * 处理单个返回
     *
     * @param list 查询结果
     * @param <T>  结果对象类型
     * @return 查询的唯一一个结果
     * @throws HibernateException 结果数量大于1的时候
     */
    protected <T> T dealWithSingle(List<T> list) throws HibernateException {
        if (list == null || list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            throw new HibernateException("结果集数量: " + list.size() + "大于1");
        } else {
            return list.get(0);
        }
    }

}
