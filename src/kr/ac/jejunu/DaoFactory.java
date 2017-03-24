package kr.ac.jejunu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ghost9087 on 24/03/2017.
 */
//이런식으로 dependency를 관리해 주는 Framework이 Spring framework
@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {//Spring에서의 DAO
        return new UserDao(connectionMaker());
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new JejuConnectionMaker();
    }
}
