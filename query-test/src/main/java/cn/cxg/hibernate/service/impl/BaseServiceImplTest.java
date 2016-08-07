package cn.cxg.hibernate.service.impl;

import cn.cxg.hibernate.BaseTestCase;
import cn.cxg.hibernate.domain.Customer;
import cn.cxg.hibernate.query.Query;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.List;

/**
 * Service Test
 * @author chenxianguan on 2016/8/7.
 */
public class BaseServiceImplTest extends BaseTestCase {

    /**
     * Logger for debugging purposes
     */
    private Logger logger = Logger.getLogger( BaseServiceImplTest.class );


    /**
     * Tests to make sure that we can add
     */
    @Test
    public void testAddCustomer()
    {
        // Create a customer
        Customer customer = new Customer();
        customer.setFirstName( "Test-First" );
        customer.setLastName( "Test-Last" );
        customer.setEmail( "test@test.com" );
        customer.setPassword( "t3st" );

        // Add a customer to the database
        baseService.save( customer );

        // Load the customer into another object
        Customer customer2 = baseService.get(Customer.class, customer.getId() );
        Assert.assertNotNull( "The customer that was created was unable to be loaded from the database",
                customer2 );

        // Assert that the customer exists
        Assert.assertEquals( "First names do not match", "Test-First", customer2.getFirstName() );
        Assert.assertEquals( "Last names do not match", "Test-Last", customer2.getLastName() );
        Assert.assertEquals( "Email addresses do not match", "test@test.com", customer2.getEmail() );
        Assert.assertEquals( "Passwords do not match", "t3st", customer2.getPassword() );

        // Remove the customer from the database
        baseService.delete(  customer2 );

        // Assert that the customer is no longer in the database
        Customer customer3 = baseService.get(Customer.class, customer.getId() );
        logger.info( "===========================Customer3: " + customer3 );
        Assert.assertNull( "The customer should have been deleted but it was not", customer3 );
    }

    /**
     * Tests querying customers by their last name
     */
    @Test
    public void testQueryByLastName()
    {
        // Create four customers
        Customer steve = new Customer( "Steven", "Haines", "steve@gomash.com", "mypass" );
        Customer linda = new Customer( "Linda", "Haines", "linda@gomash.com", "mypass" );
        Customer michael = new Customer( "Michael", "Haines", "michael@gomash.com", "mypass" );
        Customer someone = new Customer( "Someone", "Else", "someone@somewhere.com", "notmypass" );

        // Add the four customers to the database
        baseService.save( steve );
        baseService.save( linda );
        baseService.save( michael );
        baseService.save( someone );

        // Query the database
        List<Customer> customers = baseService.findByJPA("from Customer where lastName=?1", "Haines" );

        // Assert that we found all of the records that we expected to find
        Assert.assertEquals( "Did not find the three customers we inserted into the database", 3,
                customers.size() );

        // Debug
        if( logger.isDebugEnabled() )
        {
            logger.debug( "All customers with a lastname of Haines:" );
            for( Customer customer : customers )
            {
                logger.debug( "Customer: " + customer );
            }
        }

        // Clean up
        baseService.delete( steve );
        baseService.delete( linda );
        baseService.delete( michael );
        baseService.delete( someone );
    }


    @Test
    public void testQueryCount(){
        Query query = new Query(Customer.class);
        int total = baseService.count(query);
        Assert.assertEquals(0, total);
    }

}
