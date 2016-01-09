package de.netpage.sample.database.update.standalone;

import de.netpage.sample.database.update.Updatable;
import de.netpage.sample.database.update.UpdateException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Ausführung der Datenbank-Updates mittels Liquibase.
 *
 * @author Denis Wirries
 * @version 3.0
 * @since 14.02.14
 */
public class LiquibaseUpdater implements Updatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseUpdater.class);

    private final String changelogFile;
    private final DatabaseConnectable connectable;

    /**
     * Erstellt einen neuen Liquibase-Updater. Die Updates werden über
     * das Properties-File <code>database/changelog.properties</code> geladen.
     * Die Properties-Datei liegt innerhalb des Classpath.
     *
     * @param connectable Stellt die Verbindung zur Datenbank her.
     */
    public LiquibaseUpdater(DatabaseConnectable connectable) {
        this(connectable, "database/changelog.properties");
    }

    /**
     * Erstellt einen neuen Liquibase-Updater. Die Updates werden über
     * das angegebene Properties-File geladen. Die Properties-Datei liegt
     * innerhalb des Classpath.
     *
     * @param connectable   Stellt die Verbindung zur Datenbank her.
     * @param changelogFile Properties-Datei für die Updates
     */
    public LiquibaseUpdater(DatabaseConnectable connectable, String changelogFile) {
        InputStream stream = ClassLoader.getSystemResourceAsStream(changelogFile);
        if (stream == null) throw new IllegalArgumentException("Properties " + changelogFile + " not exists");

        this.connectable = connectable;
        this.changelogFile = changelogFile;
    }

    /**
     * Führt die Aktualisierung der Datenbank durch.
     */
    public void update() throws UpdateException {
        try {
            final Connection connection = connectable.getConnection();
            final Database database = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            final List<String> updateResources = loadUpdateList();
            for (final String ur : updateResources)
                doUpdate(database, ur);

            LOGGER.info("Aktualisierung erfolgreich");
            connection.close();
        } catch (final Exception ex) {
            LOGGER.error("Datenbank konnte nicht aktualisiert werden", ex);
            throw new UpdateException("Datenbank konnte nicht aktualisiert werden", ex);
        }
    }


    /**
     * Führt die Aktualisierung der Datenbank mit der angegebenen Resource für das Update-File durch.
     *
     * @param database       Datenbank
     * @param updateResource Resource mit der Datenbank.
     * @throws LiquibaseException Fehler beim Aktualisieren
     */
    private void doUpdate(final Database database, final String updateResource) throws LiquibaseException {
        LOGGER.info("Aktualisiere Datenbank mit " + updateResource);
        final Liquibase liquibase = new Liquibase(updateResource, new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    /**
     * Lädt die Liste der Updates.
     *
     * @return Liste mit Update-Ressourcen
     */
    protected List<String> loadUpdateList() throws UpdateException {
        final List<String> list = new ArrayList<String>();

        // Lade Properties Files
        final Properties props = new Properties();
        try {
            props.load(ClassLoader.getSystemResourceAsStream(changelogFile));
        } catch (final IOException ex) {
            LOGGER.error("Updates konnten nicht eingelesen werden", ex);
            throw new UpdateException("Updates konnten nicht eingelesen werden", ex);
        }

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
