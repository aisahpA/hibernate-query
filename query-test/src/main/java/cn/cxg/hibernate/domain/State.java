package cn.cxg.hibernate.domain;

import java.io.Serializable;

public class State extends DomainObject {

    private Integer id;

    public Country getCountry() {
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
