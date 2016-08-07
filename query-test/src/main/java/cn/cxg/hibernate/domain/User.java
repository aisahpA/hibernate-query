package cn.cxg.hibernate.domain;

import java.io.Serializable;

public class User extends DomainObject {

    private Integer id;

    public City getCity() {
        return null;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
