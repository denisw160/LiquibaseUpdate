package de.netpage.sample.database.update.spring;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Diese Klasse ist für die Ausführung der Spring Tests zuständig.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 21.08.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration("classpath:spring/test-context.xml")
public abstract class AbstractSpringTests extends AbstractTransactionalJUnit4SpringContextTests {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringTests.class);

    private static final String DATABASE_FOLDER = "./database/spring";

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
     * Nach dem Durchlauf der Testfälle ausführen.
     */
    @AfterClass
    public static void shutdown() {
        // nothing
    }

    /**
     * Vor jedem Testfall ausführen.
     *
     * @throws Exception Fehler
     */
    @Before
    public void setUp() throws Exception {
        // nothing
    }

    /**
     * Nach jedem Testfall ausführen.
     *
     * @throws Exception Fehler
     */
    @After
    public void tearDown() throws Exception {
        // nothing
    }

}
