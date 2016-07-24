package cn.cxg.hibernate.dao.impl;

import cn.cxg.hibernate.dao.IBaseDao;
import cn.cxg.hibernate.domain.IDomainObject;
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
    public void refresh(final Object entity) {
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

    private class QueryParam {
        private List<String> paramNames;
        private List<Object> values;

        List<String> getParamNames() {
            return paramNames;
        }

        void setParamNames(List<String> paramNames) {
            this.paramNames = paramNames;
        }

        List<Object> getValues() {
            return values;
        }

        void setValues(List<Object> values) {
            this.values = values;
        }

        String[] getParamNamesArr() {
            if (paramNames == null || paramNames.isEmpty()) {
                return null;
            }
            return paramNames.toArray(new String[paramNames.size()]);
        }

        Object[] getValuesArr() {
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.toArray(new Object[values.size()]);
        }
    }

    private QueryParam create(Map<String, Object> paramsMap) {
        QueryParam queryParam = new QueryParam();

        if (paramsMap != null && !paramsMap.isEmpty()) {
            List<String> paramNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            paramsMap.entrySet().stream()
                    .filter(param -> (param.getKey() != null) && (param.getValue() != null))
                    .forEach(param -> {
                        paramNames.add(param.getKey());
                        values.add(param.getValue());
                    });

            if (!paramNames.isEmpty()) {
                queryParam.setParamNames(paramNames);
                queryParam.setValues(values);
            }
        }
        return queryParam;
    }

    private QueryParam create(Object... params) {
        QueryParam queryParam = new QueryParam();

        if (params != null && params.length > 0) {
            List<String> paramNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (int i = 0; i < params.length; i++) {
                paramNames.add(String.valueOf(i + 1));
                values.add(params[i]);
            }

            if (!paramNames.isEmpty()) {
                queryParam.setParamNames(paramNames);
                queryParam.setValues(values);
            }
        }
        return queryParam;
    }

    private <T> List<T> findByNamedParam(String hqlString, QueryParam queryParam) {
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) this.getHibernateTemplate().findByNamedParam(hqlString,
                queryParam.getParamNamesArr(), queryParam.getValuesArr());
        return list;
    }

    /**
     * 设置Query的参数
     *
     * @param query  org.hibernate.Query
     * @param params 待设置的参数
     * @since chenxianguan 2015年12月3日下午4:38:49
     */
    private void setParams(org.hibernate.Query query, Object... params) {
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
     * @since chenxianguan 2015年12月3日下午4:38:52
     */
    private void setParams(org.hibernate.Query query, Map<String, Object> paramsMap) {
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
     * @since chenxianugan 2016年7月6日
     */
    private <T> T dealWithSingle(List<T> list) throws HibernateException {
        if (list == null || list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            throw new HibernateException("结果集数量: " + list.size() + "大于1");
        } else {
            return list.get(0);
        }
    }


    @Override
    public <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException {
        return dealWithSingle(findByJPA(hqlString, params));
    }

    @Override
    public <T> List<T> findByJPA(String hqlString, Object... params) {
        return findByNamedParam(hqlString, create(params));
    }

    @Override
    public <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap) {
        return findByNamedParam(hqlString, create(paramsMap));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap,
                                 Integer firstResult, Integer maxResults) {
        org.hibernate.Query query = this.currentSession().createQuery(hqlString);
        this.setParams(query, paramsMap);
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page search(String hqlString, Map<String, Object> paramsMap, Page page) {

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

    private int count(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(this.currentSession());
        return this.count(criteria);
    }

    private int count(Criteria criteria) {
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
    private <T> Page<T> search(DetachedCriteria detachedCriteria, Page<T> page) {
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
    // Convenience finder methods for SQL strings
    //-------------------------------------------------------------------------

    @Override
    public int executeUpdateBySql(String sql, Object... params) {
        SQLQuery queryObject = this.currentSession().createSQLQuery(sql);
        setParams(queryObject, params);
        return queryObject.executeUpdate();
    }

    @Override
    public int executeUpdateBySql(String sql, Map<String, Object> paramsMap) {
        SQLQuery queryObject = this.currentSession().createSQLQuery(sql);
        setParams(queryObject, paramsMap);
        return queryObject.executeUpdate();
    }


    @Override
    public List findBySql(String sql, Object... params) {
        return this.findBySql(null, sql, params);
    }

    @Override
    public List findBySql(String sql, Map<String, Object> paramsMap) {
        return this.findBySql(null, sql, paramsMap, null, null);
    }

    @Override
    public List findBySql(String sql, Map<String, Object> paramsMap, Integer firstResult, Integer maxResults) {
        return this.findBySql(null, sql, paramsMap, firstResult, maxResults);
    }

    @Override
    public List findBySql(Class clazz, String sql, Object[] params) {
        SQLQuery query = this.currentSession().createSQLQuery(sql);
        if (clazz != null) {
            query.addEntity(clazz);
        }
        this.setParams(query, params);
        return query.list();
    }

    @Override
    public List findBySql(Class clazz, String sql, Map<String, Object> paramsMap) {
        return this.findBySql(clazz, sql, paramsMap, null, null);
    }

    @Override
    public List findBySql(Class clazz, String sql, Map<String, Object> paramsMap, Integer firstResult, Integer maxResults) {
        SQLQuery query = this.currentSession().createSQLQuery(sql);
        if (clazz != null) {
            query.addEntity(clazz);
        }
        this.setParams(query, paramsMap);
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query.list();
    }

}
