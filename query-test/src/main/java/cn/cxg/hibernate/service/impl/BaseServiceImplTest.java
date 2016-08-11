package cn.cxg.hibernate.service.impl;

import cn.cxg.hibernate.BaseTestCase;
import cn.cxg.hibernate.domain.Customer;
import cn.cxg.hibernate.domain.Order;
import cn.cxg.hibernate.query.Query;
import cn.cxg.hibernate.query.WebQueryCriteria;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Test
 *
 * @author chenxianguan on 2016/8/7.
 */
public class BaseServiceImplTest extends BaseTestCase {

    /**
     * Logger for debugging purposes
     */
    private Logger logger = Logger.getLogger(BaseServiceImplTest.class);

    /**
     * Tests to make sure that we can add
     */
    @Test
    public void testAddCustomer() {
        // Create a customer
        Customer customer = new Customer();
        customer.setFirstName("Test-First");
        customer.setLastName("Test-Last");
        customer.setEmail("test@test.com");
        customer.setPassword("t3st");

        // Add a customer to the database
        baseService.save(customer);

        // Load the customer into another object
        Customer customer2 = baseService.get(Customer.class, customer.getId());
        Assert.assertNotNull("The customer that was created was unable to be loaded from the database",
                customer2);

        // Assert that the customer exists
        Assert.assertEquals("First names do not match", "Test-First", customer2.getFirstName());
        Assert.assertEquals("Last names do not match", "Test-Last", customer2.getLastName());
        Assert.assertEquals("Email addresses do not match", "test@test.com", customer2.getEmail());
        Assert.assertEquals("Passwords do not match", "t3st", customer2.getPassword());

        // Remove the customer from the database
        baseService.delete(customer2);

        // Assert that the customer is no longer in the database
        Customer customer3 = baseService.get(Customer.class, customer.getId());
        Assert.assertNull("The customer should have been deleted but it was not", customer3);
    }

    /**
     * Tests querying customers by their last name
     */
    @Test
    public void testQueryByLastName() {
        // Create four customers
        Customer steve = new Customer("Steven2", "Haines2", "steve@gomash.com", "mypass");
        Customer linda = new Customer("Linda2", "Haines2", "linda@gomash.com", "mypass");
        Customer michael = new Customer("Michael2", "Haines2", "michael@gomash.com", "mypass");
        Customer someone = new Customer("Someone2", "Else2", "someone@somewhere.com", "notmypass");

        // Add the four customers to the database
        baseService.save(steve);
        baseService.save(linda);
        baseService.save(michael);
        baseService.save(someone);

        // Query the database
        List<Customer> customers = baseService.findByJPA("from Customer where lastName=?1", "Haines2");

        // Assert that we found all of the records that we expected to find
        Assert.assertEquals("Did not find the three customers we inserted into the database", 3,
                customers.size());

        // Debug
        if (logger.isDebugEnabled()) {
            logger.debug("All customers with a lastname of Haines2:");
            for (Customer customer : customers) {
                logger.debug("Customer: " + customer);
            }
        }

        // Clean up
        baseService.delete(steve);
        baseService.delete(linda);
        baseService.delete(michael);
        baseService.delete(someone);
    }

    @Test
    public void testQueryCount() {
        Query query = new Query(Customer.class);
        int total = baseService.count(query);
        Assert.assertEquals(4, total);
    }

    @Test
    public void testQueryList() {
        Query query = new Query(Order.class);
        query.addCondition("customer.firstName", WebQueryCriteria.joint_eq, "Steven");
        int total = baseService.count(query);
        Assert.assertEquals(1, total);

    }

    @Test
    public void testGet() {
        Order order = baseService.get(Order.class, 1L);
        Assert.assertNotNull(order);
    }

    @Test
    public void testLoad() {
        Order order = baseService.get(Order.class, 1L);
        Assert.assertNotNull(order.getCustomer().getAddresses());
    }

    @Test
    public void testUpdate() {
        Order order = baseService.get(Order.class, 1L);
        order.setTax(0.5);
        baseService.update(order);
        Assert.assertEquals(0.5, order.getTax(), 0.000001);
    }

    @Test
    public void testDelete() {
        Order order = baseService.get(Order.class, 1L);
        Assert.assertNotNull(order);

        baseService.delete(order);
    }

    @Test
    public void testFindByHql() {
        String hql = "from Customer where lastName=:lastName";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("lastName", "Haines");

        List<Customer> customers = baseService.findByHql(hql, paramsMap);
        Assert.assertEquals(3, customers.size());
    }

    @Test
    public void testBulkUpdate() {
        baseService.updateBulkByJPA("update Order set total=?1 where id=?2", 1000D, 1L);

        Double total = baseService.findSingleByJPA("select total from Order where id=?1", 1L);
        Assert.assertEquals(1000D, total, 0.0000001);

        Double total2 = baseService.get(Order.class, 1L).getTotal();
        Assert.assertEquals(1000D, total2, 0.0000001);
    }

    @Test
    public void testQueryLike() {
        Query query = new Query(Customer.class);
        query.addCondition("lastName", WebQueryCriteria.joint_like, "Haines");
        List<Customer> customers = baseService.search(query);
        Assert.assertEquals(3, customers.size());
    }
}
