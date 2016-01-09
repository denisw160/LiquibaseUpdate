package de.netpage.sample.database.update.standalone;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

import java.sql.Connection;

/**
 * Utility-Klasse für den Zugriff auf die Datenbank. Die SessionFactory wird als Singleton bereitgestellt.
 *
 * @author Denis Wirries
 * @since 14.02.14
 */
final class HibernateUtil implements DatabaseConnectable {

    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

    private SessionFactory sessionFactory;

    /**
     * Erstellt das HibernateUtility.
     * Die Datenbank wird im Standard-Verzeichnis <code>./database/hibernate</code>
     * erstellt.
     */
    public HibernateUtil() {
        // keine Initialisierung - LazyInit beim Zugriff auf die SessionFactory.
    }

    /**
     * Erstellt die Sessionfactory aus der hibernate.cfg.xml..
     */
    @SuppressWarnings("deprecation")
    public synchronized void buildSessionFactory() {
        if (sessionFactory != null) return;
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            final Configuration config = new Configuration();
            config.configure();
            sessionFactory = config.buildSessionFactory();
        } catch (final Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            LOGGER.fatal("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Zerstört die SessionFactory und gibt die Ressourcen wieder frei.
     */
    public synchronized void destroySessionFactory() {
        if (sessionFactory == null) return;
        sessionFactory.close();
        sessionFactory = null;
    }

    /**
     * Liefert die Verbindung zur Datenbank zurück.
     *
     * @return Datenbank-Connection
     */
    public Connection getConnection() {
        return ((SessionImpl) getSession()).connection();
    }

    /**
     * Liefert die SessionFactory zurück. Ist die SessionFactory nicht vorhanden, so wird dieser erstellt.
     *
     * @return SessionFactory
     */
    public synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) buildSessionFactory();
        return sessionFactory;
    }

    /**
     * Liefert die aktuelle Session zurück.
     *
     * @return Hibernate Session
     */
    public Session getSession() {
        return getSessionFactory().openSession();
    }

}
