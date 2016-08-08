package cn.cxg.hibernate.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单详情
 *
 * @author chenxianguan on 2016/8/8.
 */
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem extends DomainObject {

    @Id
    @GeneratedValue
    private Long id;
    private Integer quantity;
    private Integer sort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem() {
    }

    public OrderItem(Integer quantity, Integer sort, Order order, Product product) {
        this.quantity = quantity;
        this.sort = sort;
        this.order = order;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
}
