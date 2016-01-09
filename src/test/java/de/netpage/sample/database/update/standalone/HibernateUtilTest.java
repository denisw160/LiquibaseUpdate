package de.netpage.sample.database.update.standalone;

import org.hibernate.SessionFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Testfälle für HibernateUtil.
 */
public class HibernateUtilTest extends AbstractHibernateTests {

    @Test
    public void testGetSessionFactory() throws Exception {
        final HibernateUtil util = getHibernateUtil();
        final SessionFactory sessionFactory = util.getSessionFactory();
        assertNotNull(sessionFactory);
    }

}