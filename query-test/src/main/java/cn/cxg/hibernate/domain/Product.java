package cn.cxg.hibernate.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 产品
 *
 * @author chenxianguan on 2016/8/8.
 */
@Entity
@Table(name = "PRODUCT")
public class Product extends DomainObject {

    @Id
    @GeneratedValue
    private Long id;
    private Double price;
    private String description;
    private Integer inventory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private Set<ProductCategory> categories = new HashSet<>();

    public Product() {
    }

    public Product(Double price, String description, Integer inventory) {
        this.price = price;
        this.description = description;
        this.inventory = inventory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Set<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<ProductCategory> categories) {
        this.categories = categories;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
