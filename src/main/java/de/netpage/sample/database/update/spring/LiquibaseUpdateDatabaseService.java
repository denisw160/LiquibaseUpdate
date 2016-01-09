package de.netpage.sample.database.update.spring;

import de.netpage.sample.database.update.UpdateException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static de.netpage.sample.database.update.spring.PropertyUtils.loadPropertiesFromClassPath;

/**
 * Manager zur Verwaltung und Aktualisierung von Datenbank-Updates.
 * Beim Einsatz als Spring Bean wird diese Aktualisierung direkt nach der Instanzierung
 * der Bean durchgeführt.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 16.08.14
 */
public class LiquibaseUpdateDatabaseService implements InitializingBean, UpdateDatabaseService {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseUpdateDatabaseService.class);

    @Autowired
    private DataSource dataSource;

    private String changeTasksFile = "database/changelog.properties";
    private boolean excuteOnLoad = true;
    private String[] changeTaskFiles;

    /**
     * Standard-Construktur.
     */
    public LiquibaseUpdateDatabaseService() {
        LOG.debug("Creating new instance of LiquibaseUpdateDatabaseService");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExcuteOnLoad(final boolean excuteOnLoad) {
        this.excuteOnLoad = excuteOnLoad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChangeTasks(final String changeTasksFile) {
        this.changeTasksFile = changeTasksFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChangeTaskFiles(final String[] changeTaskFiles) {
        if (changeTaskFiles != null) {
            this.changeTaskFiles = Arrays.copyOf(changeTaskFiles, changeTaskFiles.length);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (excuteOnLoad) update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() throws UpdateException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            final DatabaseFactory databaseFactory = DatabaseFactory.getInstance();
            final Database database = databaseFactory.findCorrectDatabaseImplementation(new JdbcConnection(connection));

            LOG.info("Start updating database ...");
            final List<String> updateResources = loadUpdateList();
            for (final String ur : updateResources)
                doUpdate(database, ur);

            LOG.info("Update completed");

        } catch (final Exception ex) {
            throw new UpdateException("Error while updating database", ex);
        } finally {
            close(connection);
        }
    }

    /**
     * Schließt die Datenbank-Verbindung.
     *
     * @param connection Datenbank-Verbindung
     */
    private void close(final Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (final SQLException e) {
            LOG.debug("Error while closing connection", e);
        }
    }

    /**
     * Führt die Aktualisierung der Datenbank mit der angegebenen Resource für das Update-File durch.
     *
     * @param database       Datenbank
     * @param updateResource Resource mit der Datenbank.
     * @throws liquibase.exception.LiquibaseException Fehler beim Aktualisieren
     */
    private void doUpdate(final Database database, final String updateResource) throws LiquibaseException {
        LOG.info("Update database with " + updateResource);
        final Liquibase liquibase = new Liquibase(updateResource, new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    /**
     * Lädt die Liste der Updates.
     *
     * @return Liste mit Update-Ressourcen
     */
    protected List<String> loadUpdateList() {
        if (changeTaskFiles != null && changeTaskFiles.length > 0) {
            return Arrays.asList(changeTaskFiles);
        }

        // Lade Properties Files
        final List<String> list = new ArrayList<String>();
        final Properties props = loadPropertiesFromClassPath(changeTasksFile);

        // Da Keys als HashMap gespeichert werden, ist die Reihenfolge nicht gewährleistet
        final List<String> keys = new ArrayList<String>();
        for (final Object key : props.keySet()) {
            keys.add((String) key);
        }
        Collections.sort(keys);

        // Schlüssel in richtiger Reihenfolge auslesen
        for (final String key : keys) {
            list.add(props.getProperty(key));
        }

        return list;
    }

}
