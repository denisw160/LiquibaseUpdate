package de.netpage.sample.database.update;


/**
 * Interface für die Defintion von UpdateServices.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 20.08.2014
 */
public interface Updatable {


    /**
     * Führt die Aktaulisierung der Dateien und Verzeichnisse durch.
     *
     * @throws UpdateException Exception wird geworfen, wenn die Aktualisierung nicht vollständig war
     */
    void update() throws UpdateException;

}
