package de.netpage.sample.database.update.standalone;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Testfälle für LiquibaseUpdater.
 */
public class LiquibaseUpdaterTest extends AbstractHibernateTests {

    private LiquibaseUpdater update;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        update = new LiquibaseUpdater(getHibernateUtil());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWrongPath() throws Exception {
        final LiquibaseUpdater liquibaseUpdater = new LiquibaseUpdater(getHibernateUtil(), "/database/notfound.properties");
        assertNotNull(liquibaseUpdater);
    }

    @Test
    public void testUpdate() throws Exception {
        Session session = null;
        try {
            session = getHibernateUtil().getSession();
            final SQLQuery sqlQuery = session.createSQLQuery("SELECT attributeC, attributeD, attributeE from tableB");
            final List list = sqlQuery.list();
            assertNotNull(list);
            fail("Table B already exsists");
        } catch (Exception ex) {
            // ignore - expected
        } finally {
            if (session != null) session.close();
        }

        // Update Empty-Database
        update.update();

        session = getHibernateUtil().getSession();
        final SQLQuery sqlQuery = session.createSQLQuery("SELECT attributeC, attributeD, attributeE from tableB");
        final List list = sqlQuery.list();
        assertNotNull(list);
        session.close();

        // update twice - has no actions to do
        update.update();
    }

}