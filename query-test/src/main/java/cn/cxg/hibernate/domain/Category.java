package cn.cxg.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 类别
 *
 * @author chenxianguan on 2016/8/7.
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends DomainObject {

    @Id
    @GeneratedValue
    private Long id;
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
