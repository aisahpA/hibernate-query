package cn.cxg.hibernate.domain;

import java.io.Serializable;

public class District extends DomainObject {

    private Integer id;

    public State getState() {
        return null;
    }

    public String getCode() {
        return null;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
