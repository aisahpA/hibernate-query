package cn.cxg.hibernate.dao.impl;

import cn.cxg.hibernate.domain.IDomainObject;
import cn.cxg.hibernate.domain.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test BaseDaoImpl
 * Created by Chenxg on 2016/7/24.
 */
public class TestBaseDaoImpl {

    @Test
    public void generic() {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        addBatch(users);
        delete(users);

    }

    void addBatch(List<? extends IDomainObject> entities){
        System.out.println(entities.size());
    }

    void delete(Collection<? extends IDomainObject> entities) {
        System.out.println(entities.size());
    }
}
