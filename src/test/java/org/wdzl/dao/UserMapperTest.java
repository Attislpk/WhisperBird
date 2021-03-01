package org.wdzl.dao;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wdzl.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;


    @Test
    void testFdeleteByPrimaryKey() {
        int effectNum = userMapper.deleteByPrimaryKey("test01");
        System.out.println(effectNum);
        effectNum = userMapper.deleteByPrimaryKey("test02");
        System.out.println(effectNum);
    }

    @Test
    void testAinsert() {
        User user = new User("test01","test01","test01","test01","test01","test01","test01","test01");
        int effectNum = userMapper.insert(user);
        System.out.println(effectNum);
    }

    @Test
    void testBinsertSelective() {
        User user = new User();
        user.setId("test02");
        user.setUsername("XXXXXXXXXXX");
        int effectNum = userMapper.insertSelective(user);
        System.out.println(effectNum);
    }

    @Test
    void testCselectByPrimaryKey() {
        User user = userMapper.selectByPrimaryKey("im01");
        System.out.println(user);
        user= userMapper.selectByPrimaryKey("test01");
        System.out.println(user);
        user = userMapper.selectByPrimaryKey("test02");
        System.out.println(user);
    }

    @Test
    void testDupdateByPrimaryKeySelective() {
        User user = new User();
        user.setUsername("updateselecttive");
        user.setId("test02");
        int effectNum = userMapper.updateByPrimaryKeySelective(user);
        System.out.println(effectNum);
    }

    @Test
    void testEupdateByPrimaryKey() {
        User user = new User("test01","update","update","update","update","update","update","update");
        int effectNum = userMapper.updateByPrimaryKey(user);
        System.out.println(effectNum);
    }
}