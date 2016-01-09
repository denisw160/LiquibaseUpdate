package de.netpage.sample.database.update.spring;


import de.netpage.sample.database.update.Updatable;

import javax.sql.DataSource;

/**
 * Dies ist das Interface für das Datenbank-Update.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 16.08.2014
 */
public interface UpdateDatabaseService extends Updatable {


    /**
     * Setzt die Datenbank-Verbindung mittels einer DataSource.
     * Diese DataSource ist erforderlich für das Update.
     *
     * @param dataSource DataSource
     */
    void setDataSource(DataSource dataSource);

    /**
     * Sollen die Updates beim Laden ausgeführt werden?
     *
     * @param excuteOnLoad Ausführen beim Start
     */
    void setExcuteOnLoad(boolean excuteOnLoad);

    /**
     * Setzt die zusammengefasste Change-Database-Datei, die alle Updates bundelt.
     * Wird keine Datei angegeben, so wird die Standard-Einstellung verwendet.
     *
     * @param changeTasksFile Properties-Datei, siehe META-INF/database/changelog.properties
     */
    void setChangeTasks(String changeTasksFile);

    /**
     * Setzt die Liste der Updates direkt. Hier werden die XML-Dateien mit den
     * Change-Database-Daten angegeben. Ist die Liste gesetzt, werden die Updates aus
     * der Changelog-Datei ignoriert.
     *
     * @param changeTaskFiles Liste mit den Changelogs, siehe META-INF/database/update-database-0.3.x.xml
     */
    void setChangeTaskFiles(String[] changeTaskFiles);

}
