package de.netpage.sample.database.update.standalone;

import java.sql.Connection;

/**
 * Kann eine Datenbanb-Connection herstellen und diese zurückliefern.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 26.08.2015
 */
public interface DatabaseConnectable {

    /**
     * Liefert die Datenbank-Verbindung.
     *
     * @return Datenbank-Verbindung als SQL-Connection.
     */
    Connection getConnection();

}
