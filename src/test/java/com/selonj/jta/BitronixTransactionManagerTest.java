package com.selonj.jta;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.jndi.BitronixInitialContextFactory;
import org.junit.After;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Hashtable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by L.x on 16-2-25.
 */
public class BitronixTransactionManagerTest {

    public static final String TRANSACTION_NAME = "java:comp/UserTransaction";

    @After
    public void tearDown() throws Exception {
        TransactionManagerServices.getTransactionManager().shutdown();
    }

    @Test
    public void fetchByTransactionManagerServices() throws Exception {
        assertNotNull(TransactionManagerServices.getTransactionManager());
    }

    @Test
    public void fetchByJNDI() throws Exception {
        InitialContext context = new InitialContext(new Hashtable<String, String>() {{
            put(Context.INITIAL_CONTEXT_FACTORY, BitronixInitialContextFactory.class.getCanonicalName());
        }});

        Object transactionManager = context.lookup(TRANSACTION_NAME);

        assertThat(transactionManager, instanceOf(TransactionManager.class));
        assertThat(transactionManager, instanceOf(UserTransaction.class));
    }

    @Test
    public void transactionLifecycle() throws Exception {
        TransactionManager transactionManager = TransactionManagerServices.getTransactionManager();

        assertNull(transactionManager.getTransaction());

        transactionManager.begin();
        assertNotNull(transactionManager.getTransaction());

        transactionManager.commit();
        assertNull(transactionManager.getTransaction());

        transactionManager.begin();
        transactionManager.rollback();
        assertNull(transactionManager.getTransaction());
    }
}
