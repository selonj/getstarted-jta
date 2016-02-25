package com.selonj.jta;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.selonj.supports.Counter;
import com.selonj.supports.MySQLXADataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.*;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by L.x on 16-2-25.
 */
public class SingletonDataSourceTransactionTest {
    private Counter counter;
    private BitronixTransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        counter = new Counter(MySQLXADataSource.of("mysql", "test"));
        transactionManager = TransactionManagerServices.getTransactionManager();

        transactionManager.begin();
        counter.wipe();
        transactionManager.commit();
    }

    @After
    public void tearDown() throws Exception {
        counter.close();
        transactionManager.shutdown();
    }

    @Test
    public void commitTransaction() throws Exception {
        assertThat(countOfRows(), equalTo(0));

        transactionManager.begin();
        counter.insertNewLine();
        transactionManager.commit();

        assertThat(countOfRows(), equalTo(1));
    }

    @Test
    public void rollbackTransaction() throws Exception {
        assertThat(countOfRows(), equalTo(0));

        transactionManager.begin();
        counter.insertNewLine();
        transactionManager.rollback();

        assertThat(countOfRows(), equalTo(0));
    }

    @Test
    public void transactionOnFailing() throws Exception {
        assertThat(countOfRows(), equalTo(0));

        transactionManager.begin();
        counter.insertFailed();
        transactionManager.commit();

        assertThat(countOfRows(), equalTo(0));
    }


    private int countOfRows() throws SQLException, HeuristicMixedException, RollbackException, SystemException, HeuristicRollbackException, NotSupportedException {
        transactionManager.begin();
        try {
            return counter.countOfRows();
        } finally {
            transactionManager.commit();
        }
    }

}
