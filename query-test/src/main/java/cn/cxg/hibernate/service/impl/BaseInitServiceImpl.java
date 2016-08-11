package cn.cxg.hibernate.service.impl;

import cn.cxg.hibernate.domain.*;
import org.springframework.test.annotation.Rollback;

/**
 *
 * Created by chenxianguan on 2016/8/8.
 */
public class BaseInitServiceImpl extends BaseServiceImpl{


    /**
     * 初始化测试使用的数据
     */
    @Rollback(value = false)
    public void saveInit(){
        Long total = baseDao.findSingleByJPA("select count(1) from Customer");
        if (total > 0){
            return;
        }

        // Create four customers
        Customer steve = new Customer( "Steven", "Haines", "steve@gomash.com", "mypass" );
        Customer linda = new Customer( "Linda", "Haines", "linda@gomash.com", "mypass" );
        Customer michael = new Customer( "Michael", "Haines", "michael@gomash.com", "mypass" );
        Customer someone = new Customer( "Someone", "Else", "someone@somewhere.com", "notmypass" );

        // Add the four customers to the database
        baseDao.save( steve );
        baseDao.save( linda );
        baseDao.save( michael );
        baseDao.save( someone );

        // Add address
        Address steveAddress = new Address();
        steveAddress.setAddress1("steve Address1");
        steveAddress.setAddress2("steve Address2");
        steveAddress.setCity("成都");
        steveAddress.setZip("6100000");
        steveAddress.setCustomer(steve);
        baseDao.save(steveAddress);

        // Add product
        Product product1 = new Product(2.0, "产品一", 20);
        Product product2 = new Product(5.0, "产品二", 10);
        baseDao.save(product1);
        baseDao.save(product2);

        // Add order and orderItem
        Order order1 = new Order(1L, 10.0, 1.0, 11.0);
        order1.setCustomer(steve);
        order1.setAddress(steveAddress);

        OrderItem item1 = new OrderItem(1, 1, order1, product1);
        OrderItem item2 = new OrderItem(1, 2, order1, product2);

        order1.getOrderItems().add(item1);
        order1.getOrderItems().add(item2);
        baseDao.save(order1);
    }

    public void deleteClear(){
        baseDao.bulkUpdate("delete from OrderItem");
        baseDao.bulkUpdate("delete from Order");
        baseDao.bulkUpdate("delete from Address");
        baseDao.bulkUpdate("delete from ProductCategory");
        baseDao.bulkUpdate("delete from Category");
        baseDao.bulkUpdate("delete from Product");
        baseDao.bulkUpdate("delete from Customer");
    }
}
