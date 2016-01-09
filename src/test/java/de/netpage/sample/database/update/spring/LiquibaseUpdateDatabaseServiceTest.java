package de.netpage.sample.database.update.spring;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

/**
 * Testfälle für LiquibaseUpdateDatabaseService.
 */
public class LiquibaseUpdateDatabaseServiceTest extends AbstractSpringTests {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseUpdateDatabaseServiceTest.class);

    @Autowired
    private DataSource dataSource;

    @Test
    public void testUpdate() throws Exception {
        // Methode: update wird beim Hochfahren des Spring Context ausgeführt.
        testUpdateResult();
    }

    @Test
    public void testManuelFile() throws Exception {
        final UpdateDatabaseService updater = new LiquibaseUpdateDatabaseService();
        updater.setDataSource(dataSource);
        updater.setChangeTasks("database/changelog.properties");
        updater.update();

        testUpdateResult();
    }

    @Test
    public void testManuelFiles() throws Exception {
        final UpdateDatabaseService updater = new LiquibaseUpdateDatabaseService();
        updater.setDataSource(dataSource);
        final String[] files = new String[]{"database/changelog.0.1.0.xml"};
        updater.setChangeTaskFiles(files);
        updater.update();

        testUpdateResult();
    }

    @Test
    public void testManuelFiles2() throws Exception {
        final UpdateDatabaseService updater = new LiquibaseUpdateDatabaseService();
        updater.setDataSource(dataSource);
        final String[] files = new String[]{};
        updater.setChangeTaskFiles(files);
        updater.update();

        testUpdateResult();
    }

    private void testUpdateResult() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT count(*) FROM tableB");
            while (resultSet.next()) {
                assertEquals(3, resultSet.getInt(1));
            }
        } finally {
            closeQuietly(resultSet);
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    private void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable == null) return;
        try {
            autoCloseable.close();
        } catch (final Exception e) {
            LOG.trace("Error while closing", e);
        }
    }

}