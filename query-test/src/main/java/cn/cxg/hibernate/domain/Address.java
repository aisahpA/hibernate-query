package cn.cxg.hibernate.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 地址
 * @author chenxianguan on 2016/8/7.
 */
@Entity
@Table(name = "ADDRESS")
public class Address extends DomainObject {

    @Id
    @GeneratedValue
    private Long id;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Serializable getPrimaryKey() {
        return null;
    }
}
