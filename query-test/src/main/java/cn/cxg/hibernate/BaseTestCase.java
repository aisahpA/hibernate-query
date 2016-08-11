package cn.cxg.hibernate;

import cn.cxg.hibernate.service.BaseService;
import cn.cxg.hibernate.service.impl.BaseInitServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 测试基础
 * <br/>当初始化插入数据回滚事务的时候，需要先将@Transactional注释掉，然后运行一个测试方法，最后再取消注释
 *
 * @author chenxianguan on 2016/8/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@Transactional
public class BaseTestCase {

    @Resource(name = "baseService")
    protected BaseService baseService;

    @Resource(name = "initService")
    private BaseInitServiceImpl initService;


    @Before
    public void setUp() {
        //初始化数据
        initService.saveInit();
    }


    @After
    public void tearDown() {

    }



}
