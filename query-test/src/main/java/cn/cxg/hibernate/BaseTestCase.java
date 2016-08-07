package cn.cxg.hibernate;

import cn.cxg.hibernate.service.BaseService;
import cn.cxg.hibernate.util.HSQL_Util;
import org.hsqldb.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 测试基础
 *
 * @author chenxianguan on 2016/8/7.
 */
public class BaseTestCase {

    /**
     * A Spring application applicationContext that we'll create from a test application applicationContext and use to create
     * our DAO object (and data source, session factory, etc.)
     */
    private static ApplicationContext applicationContext = null;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * The BaseService that we'll be testing
     */
    protected BaseService baseService;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
       // HSQL_Util.startHSQL();

        // Load the applicationContext.xml file
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Before
    public void setUp() {
        baseService = (BaseService) applicationContext.getBean("baseService");
    }

    @After
    public void tearDown() {
        baseService = null;
      //  HSQL_Util.startHSQL();
    }



}
