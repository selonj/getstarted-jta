package com.selonj.supports;

import bitronix.tm.resource.jdbc.PoolingDataSource;

import javax.transaction.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by L.x on 16-2-25.
 */
public class Counter {
    public static final String SQL_DELETE_ALL = "DELETE FROM counter";
    public static final String SQL_INSERT_ITEM = "INSERT INTO counter VALUES()";
    public static final String SQL_COUNT_ROWS = "SELECT count(*) FROM counter";
    private PoolingDataSource dataSource;
    private Connection connection;

    public Counter(PoolingDataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    public void wipe() throws SQLException, NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        execute(SQL_DELETE_ALL);
    }

    public void insertNewLine() throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        execute(SQL_INSERT_ITEM);
    }

    public void insertFailed() throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        try {
            execute("insert into counter (unknown) values (1)");
        } catch (Exception ignored) {
        }
    }

    public void execute(String sql) throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        PreparedStatement statement = getConnection().prepareStatement(sql);
        try {
            statement.executeUpdate();
        } finally {
            close(statement);
        }
    }

    public int countOfRows() throws SystemException, NotSupportedException, SQLException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        PreparedStatement statement = getConnection().prepareStatement(SQL_COUNT_ROWS);
        ResultSet results = null;
        try {
            results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } finally {
            close(results);
            close(statement);
        }
    }

    public void close() {
        close(connection);
        dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    private void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ignored) {
            }
        }
    }
}
