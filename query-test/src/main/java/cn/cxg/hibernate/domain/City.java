package cn.cxg.hibernate.domain;

import java.io.Serializable;

public class City extends DomainObject {

    private Integer id;

    public District getDistrict() {
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
