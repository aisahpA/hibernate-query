package cn.cxg.hibernate.service.impl;

import cn.cxg.hibernate.dao.BaseDao;
import cn.cxg.hibernate.domain.IDomainObject;
import cn.cxg.hibernate.query.Page;
import cn.cxg.hibernate.query.Query;
import cn.cxg.hibernate.service.BaseService;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements BaseService {

    protected BaseDao baseDao;

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public <T> T get(Class<T> entityClass, Serializable id) {
        return baseDao.get(entityClass, id);
    }

    @Override
    public Serializable save(IDomainObject entity) {
        return baseDao.save(entity);
    }

    @Override
    public void update(IDomainObject entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(IDomainObject entity) {
        baseDao.delete(entity);
    }

    @Override
    public void delete(Class<? extends IDomainObject> entityClass, Serializable id) {
        baseDao.delete(baseDao.get(entityClass, id));
    }

    @Override
    public void deleteAll(Collection<? extends IDomainObject> entities) {
        baseDao.deleteAll(new ArrayList<>(entities));
    }


    @Override
    public <T> T findSingleByJPA(String hqlString, Object... params) throws HibernateException {
        return baseDao.findSingleByJPA(hqlString, params);
    }

    @Override
    public <T> List<T> findByJPA(String hqlString, Object... params) {
        return baseDao.findByJPA(hqlString, params);
    }

    @Override
    public <T> List<T> findByHql(String hqlString, Map<String, Object> paramsMap) {
        return baseDao.findByHql(hqlString, paramsMap);
    }


    @Override
    public int count(Query query) {
        return baseDao.count(query);
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
    public int updateBulkByJPA(String queryString, Object... values) {
        return baseDao.bulkUpdateByJPA(queryString, values);
    }

}
