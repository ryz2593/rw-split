package com.ryz2593;

import com.ryz2593.entity.User;
import com.ryz2593.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Unit test for simple App.
 * @author ryz2593
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-mybatis.xml")
public class AppTest {

    @Autowired(required = false)
    private UserMapper userMapper;
    /**
     * Rigorous Test :-)
     */
    @Test
    public void selectTest() {
        List<User> userList = userMapper.selectAll();
        System.out.println(userList);
    }
}
