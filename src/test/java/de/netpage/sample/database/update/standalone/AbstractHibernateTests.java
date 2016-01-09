package de.netpage.sample.database.update.standalone;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Testcase für HibernateTests.
 */
public abstract class AbstractHibernateTests {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractHibernateTests.class);

    private static final String DATABASE_FOLDER = "./database/hibernate";

    private HibernateUtil hibernateUtil;

    /**
     * Vor dem Abarbeiten der Testfälle dieser Klasse ausführen.
     */
    @BeforeClass
    public static void init() {
        LOG.info("Lösche Database Folder {}", DATABASE_FOLDER);
        File outputDir = new File(DATABASE_FOLDER);
        if (outputDir.exists() && outputDir.isDirectory()) {
            final File[] files = outputDir.listFiles();
            if (files != null)
                for (File f : files) {
                    assertTrue("Löschen des Database Folders nicht erfolgreich", f.delete());
                }
            outputDir.deleteOnExit();
        }
    }

    /**
     * Vor jedem Testfall ausführen.
     *
     * @throws Exception Fehler
     */
    @Before
    public void setUp() throws Exception {
        hibernateUtil = new HibernateUtil();
    }

    /**
     * Nach jedem Testfall ausführen.
     *
     * @throws Exception Fehler
     */
    @After
    public void tearDown() throws Exception {
        hibernateUtil.destroySessionFactory();
        hibernateUtil = null;
    }

    /**
     * Liefert das HibernateUtil zurück.
     *
     * @return HibernateUtil
     */
    protected HibernateUtil getHibernateUtil() {
        return hibernateUtil;
    }

}
