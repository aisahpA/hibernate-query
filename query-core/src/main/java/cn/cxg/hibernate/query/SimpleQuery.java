package cn.cxg.hibernate.query;

import java.util.Collection;

/**
 * 简化Query
 *
 * @author chenxianguan on 2016/7/25.
 */
public class SimpleQuery<T> {

    private Query query;

    private T domain;

    /**
     * 构建查询对象
     *
     */
    public SimpleQuery() {
        query = new Query(domain.getClass());
    }


    public T getDomain(){
        if (domain != null) {
            return domain;
        }
        //生成一个代理对象，实现调用T的方法的时候


        return null;
    }








    public SimpleQuery eq(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_eq, fieldValue);
        return this;
    }

    public SimpleQuery ne(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_ne, fieldValue);
        return this;
    }

    public SimpleQuery like(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_like, fieldValue);
        return this;
    }

    public SimpleQuery isNull(String fieldName){
        query.addCondition(fieldName, WebQueryCriteria.joint_isNull);
        return this;
    }

    public SimpleQuery isNotNull(String fieldName){
        query.addCondition(fieldName, WebQueryCriteria.joint_isNotNull);
        return this;
    }

    public SimpleQuery gt(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_gt, fieldValue);
        return this;
    }

    public SimpleQuery lt(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_lt, fieldValue);
        return this;
    }

    public SimpleQuery le(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_le, fieldValue);
        return this;
    }

    public SimpleQuery ge(String fieldName, Object fieldValue){
        query.addCondition(fieldName, WebQueryCriteria.joint_ge, fieldValue);
        return this;
    }

    public SimpleQuery between(String fieldName, Object fieldValue, Object fieldValue1){
        query.addCondition(fieldName, WebQueryCriteria.joint_between, fieldValue, fieldValue1);
        return this;
    }

    public SimpleQuery notBetween(String fieldName, Object fieldValue, Object fieldValue1){
        query.addCondition(fieldName, WebQueryCriteria.joint_notbetween, fieldValue, fieldValue1);
        return this;
    }

    public SimpleQuery in(String fieldName, Object[] fieldValues){
        query.addCondition(fieldName, WebQueryCriteria.joint_in, fieldValues);
        return this;
    }

    public SimpleQuery in(String fieldName, Collection fieldValues){
        query.addCondition(fieldName, WebQueryCriteria.joint_in, fieldValues);
        return this;
    }




}
