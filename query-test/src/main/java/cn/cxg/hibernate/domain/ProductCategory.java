package cn.cxg.hibernate.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品类别
 *
 * @author chenxianguan on 2016/8/8.
 */
@Entity
@Table(name = "PRODUCT_CATEGORIES",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "category_id"})})
public class ProductCategory extends DomainObject {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
