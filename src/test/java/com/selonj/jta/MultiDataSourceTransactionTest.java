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
public class MultiDataSourceTransactionTest {
    private Counter counter1;
    private BitronixTransactionManager transactionManager;
    private Counter counter2;

    @Before
    public void setUp() throws Exception {
        counter1 = new Counter(MySQLXADataSource.of("test", "test"));
        counter2 = new Counter(MySQLXADataSource.of("test2", "test2"));

        transactionManager = TransactionManagerServices.getTransactionManager();

        transactionManager.begin();
        counter1.wipe();
        counter2.wipe();
        transactionManager.commit();
    }

    @After
    public void tearDown() throws Exception {
        counter1.close();
        counter2.close();
        transactionManager.shutdown();
    }

    @Test
    public void commitTransaction() throws Exception {
        assertThat(countOfRows(counter1), equalTo(0));
        assertThat(countOfRows(counter2), equalTo(0));

        transactionManager.begin();
        counter1.insertNewLine();
        counter2.insertNewLine();
        transactionManager.commit();

        assertThat(countOfRows(counter1), equalTo(1));
        assertThat(countOfRows(counter2), equalTo(1));
    }

    @Test
    public void rollbackTransaction() throws Exception {
        assertThat(countOfRows(counter1), equalTo(0));
        assertThat(countOfRows(counter2), equalTo(0));

        transactionManager.begin();
        counter1.insertNewLine();
        counter2.insertNewLine();
        transactionManager.rollback();

        assertThat(countOfRows(counter1), equalTo(0));
        assertThat(countOfRows(counter2), equalTo(0));
    }

    @Test
    public void canCommitFailingTransaction() throws Exception {
        assertThat(countOfRows(counter1), equalTo(0));
        assertThat(countOfRows(counter2), equalTo(0));

        transactionManager.begin();
        counter1.insertFailed();
        counter2.insertNewLine();
        transactionManager.commit();

        assertThat(countOfRows(counter1), equalTo(0));
        assertThat(countOfRows(counter2), equalTo(1));
    }


    private int countOfRows(Counter counter) throws SQLException, HeuristicMixedException, RollbackException, SystemException, HeuristicRollbackException, NotSupportedException {
        transactionManager.begin();
        try {
            return counter.countOfRows();
        } finally {
            transactionManager.commit();
        }
    }

}
