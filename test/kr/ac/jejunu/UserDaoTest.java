package kr.ac.jejunu;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ghost9087 on 15/03/2017.
 */
public class UserDaoTest {
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
//        daoFactory = new DaoFactory();
        //Dependency Lookup!
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("daoFactory.xml");
        userDao = context.getBean("userDao", UserDao.class);
    }

    @Test
    public void get() throws SQLException, ClassNotFoundException {
        Long id = 1L;
        String name = "김동하";
        String password = "1234";


        User user = userDao.get(id);

        assertThat(id, is(user.getId()));
        assertThat(name, is(user.getName()));
        assertThat(password, is(user.getPassword()));
    }
    @Test
    public void add() throws SQLException, ClassNotFoundException {
        User user = new User();
        String name = "불곰";
        user.setName(name);
        String password = "1111";
        user.setPassword(password);

        Long id = userDao.add(user); //리턴값이 void라도 괜찮더라도 테스트를 위해 return을 주는것을 생각해볼것
        User resultUser = userDao.get(id);

        assertThat(id, is(resultUser.getId()));
        assertThat(name, is(resultUser.getName()));
        assertThat(password, is(resultUser.getPassword()));
    }
}
