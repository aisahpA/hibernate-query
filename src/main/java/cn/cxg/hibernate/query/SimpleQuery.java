package cn.cxg.hibernate.query;

import java.util.Collection;

/**
 * 简化
 * Created by Chenxg on 2016/7/25.
 */
public class SimpleQuery extends Query {

    public SimpleQuery eq(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_eq, fieldValue);
        return this;
    }

    public SimpleQuery ne(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_ne, fieldValue);
        return this;
    }

    public SimpleQuery like(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_like, fieldValue);
        return this;
    }

    public SimpleQuery isNull(String fieldName){
        this.addCondition(fieldName, WebQueryCriteria.joint_isNull);
        return this;
    }

    public SimpleQuery isNotNull(String fieldName){
        this.addCondition(fieldName, WebQueryCriteria.joint_isNotNull);
        return this;
    }

    public SimpleQuery gt(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_gt, fieldValue);
        return this;
    }

    public SimpleQuery lt(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_lt, fieldValue);
        return this;
    }

    public SimpleQuery le(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_le, fieldValue);
        return this;
    }

    public SimpleQuery ge(String fieldName, Object fieldValue){
        this.addCondition(fieldName, WebQueryCriteria.joint_ge, fieldValue);
        return this;
    }

    public SimpleQuery between(String fieldName, Object fieldValue, Object fieldValue1){
        this.addCondition(fieldName, WebQueryCriteria.joint_between, fieldValue, fieldValue1);
        return this;
    }

    public SimpleQuery notBetween(String fieldName, Object fieldValue, Object fieldValue1){
        this.addCondition(fieldName, WebQueryCriteria.joint_notbetween, fieldValue, fieldValue1);
        return this;
    }

    public SimpleQuery in(String fieldName, Object[] fieldValues){
        this.addCondition(fieldName, WebQueryCriteria.joint_in, fieldValues);
        return this;
    }

    public SimpleQuery in(String fieldName, Collection fieldValues){
        this.addCondition(fieldName, WebQueryCriteria.joint_in, fieldValues);
        return this;
    }




}
