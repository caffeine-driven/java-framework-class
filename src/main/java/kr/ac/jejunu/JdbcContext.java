package kr.ac.jejunu;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ghost9087 on 07/04/2017.
 */
public class JdbcContext {
    private DataSource dataSource;

    public User queryForObject(String sql, Object[] params) throws SQLException {
        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }
            return preparedStatement;
        };
        return jdbcContextWithStatementStrategyForGet(statementStrategy);
    }
    public Long insert(String sql, Object[] params) {
        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 1; i <= params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }

            return preparedStatement;
        };
        return jdbcContextWithStatementStrategyForInsert(statementStrategy);
    }

    public void update(String sql, Object[] params) {
        //template(interface)/callback pattern
        //다만 interface를 항상 가지고 있어야 해서 코드가 지저분해 질 가능성도...
        //strategy패턴을 쓰다보면 이런 식이 많이 나옴.
        //lambda function에 들어가는 parameter의 type을 모르는 게 이 녀석의 강점
        //타입은 lambda function을 쓰는 녀석이 결정한다.
        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++){
                preparedStatement.setObject(i, params[i-1]);
            }
            return preparedStatement;
        };
        jdbcContextWithStatementStrategyForUpdate(statementStrategy);
    }

    public User jdbcContextWithStatementStrategyForGet(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = dataSource.getConnection();

            preparedStatement = statementStrategy.makeStatement(connection);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();//cursor를 entry point로부터 첫번째 결과가 있는 row로 이동
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }

    public Long jdbcContextWithStatementStrategyForInsert(StatementStrategy statementStrategy) {
        Connection connection = null;

        ResultSet resultSet = null;
        Long id = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();

            preparedStatement = statementStrategy.makeStatement(connection);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("SELECT last_insert_id()");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            id = resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return id;
    }


    public void jdbcContextWithStatementStrategyForUpdate(StatementStrategy statementStrategy) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();

            preparedStatement = statementStrategy.makeStatement(connection);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
