package cn.cxg.hibernate;

import cn.cxg.hibernate.domain.*;
import cn.cxg.hibernate.service.BaseService;
import cn.cxg.hibernate.service.impl.BaseInitServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private BaseInitServiceImpl initService;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // Load the applicationContext.xml file
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Before
    public void setUp() {
        baseService = (BaseService) applicationContext.getBean("baseService");

        initService = (BaseInitServiceImpl)applicationContext.getBean("initService");

        //初始化数据
        initService.saveInit();
    }


    @After
    public void tearDown() {
        initService.deleteClear();
        baseService = null;
    }



}
