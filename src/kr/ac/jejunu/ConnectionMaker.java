package kr.ac.jejunu;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ghost9087 on 24/03/2017.
 */
public interface ConnectionMaker {
    public Connection getConnection() throws ClassNotFoundException, SQLException;
}
