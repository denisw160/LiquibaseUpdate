package de.netpage.sample.database.update.spring;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Utility-Funktionen für Property-Dateien.
 *
 * @author Denis Wirries
 * @version 1.0
 * @since 16.08.2014
 */
final class PropertyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);

    /**
     * Nur statische Methoden.
     */
    private PropertyUtils() {
    }

    /**
     * Liest die Properties Datei aus einem Ordner ein.
     *
     * @param propertiesFile Properties-Datei
     * @param path           Pfad zur Datei
     * @return Properties
     */
    public static Properties loadPropertiesFromPath(final String path, final String propertiesFile) {
        return loadPropertiesFromPath(path + File.separator + propertiesFile);
    }

    /**
     * Liest die Properties Datei aus einem Ordner ein.
     *
     * @param propertiesFile Properties-Datei
     * @return Properties
     */
    public static Properties loadPropertiesFromPath(final String propertiesFile) {
        final Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(propertiesFile);
            props.load(in);
        } catch (final Exception ex) {
            LOG.error("Error while reading from path: " + propertiesFile, ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return props;
    }

    /**
     * Speichert die angegebenen Properties in der Datei.
     *
     * @param properties     Properties
     * @param path           Path
     * @param propertiesFile Dateiname
     */
    public static void storePropertiesInPath(final Properties properties,
                                             final String path, final String propertiesFile) {

        storePropertiesInPath(properties, path + File.separator + propertiesFile);
    }

    /**
     * Speichert die angegebenen Properties in der Datei.
     *
     * @param properties     Properties
     * @param propertiesFile Dateiname
     */
    private static void storePropertiesInPath(final Properties properties, final String propertiesFile) {
        OutputStream out = null;
        try {
            final File outputFile = new File(propertiesFile);
            final File outputDirectory = outputFile.getParentFile();
            if (!outputDirectory.exists()) {
                LOG.debug("Creating directory {}: {}", outputDirectory.getAbsolutePath(), outputDirectory.mkdirs());
            }

            out = new FileOutputStream(outputFile);
            properties.store(out, null);
        } catch (final Exception ex) {
            LOG.error("Error while writing to path: " + propertiesFile, ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Liest die Properties Datei aus dem Classpath ein.
     *
     * @param propertiesFile Properties-Datei
     * @return Properties
     */
    public static Properties loadPropertiesFromClassPath(final String propertiesFile) {
        final Properties props = new Properties();
        InputStream in = null;
        try {
            in = ClassLoader.getSystemResourceAsStream(propertiesFile);
            // fallback
            if (in == null)
                in = PropertyUtils.class.getClassLoader().getResourceAsStream(propertiesFile);
            props.load(in);
        } catch (final Exception ex) {
            LOG.error("Error while reading from resources: " + propertiesFile, ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return props;
    }

}
