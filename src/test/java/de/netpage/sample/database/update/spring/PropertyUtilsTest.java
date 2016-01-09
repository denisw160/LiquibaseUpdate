package de.netpage.sample.database.update.spring;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static de.netpage.sample.database.update.spring.PropertyUtils.*;
import static org.junit.Assert.*;

/**
 * Testfälle für PropertyUtils.
 */
public class PropertyUtilsTest {

    @Test
    public void testLoadPropertiesFromPath() throws Exception {
        final Properties properties = loadPropertiesFromPath("./src/test/resources/de/netpage/sample/database/update/spring", "sample.properties");
        assertNotNull(properties);
        assertEquals(2, properties.keySet().size());
        assertEquals("value2", properties.get("key2"));
    }

    @Test
    public void testLoadPropertiesFromPath2() throws Exception {
        final Properties properties = loadPropertiesFromPath("./src/test/resources/de/netpage/sample/database/update/spring/sample.properties");
        assertNotNull(properties);
        assertEquals(2, properties.keySet().size());
        assertEquals("value2", properties.get("key2"));
    }

    @Test
    public void testLoadPropertiesFromPathError() throws Exception {
        final Properties properties = loadPropertiesFromPath("./de/netpage/sample/database/update/spring/notfound.properties");
        assertNotNull(properties);
        assertEquals(0, properties.keySet().size());
    }

    @Test
    public void testStorePropertiesInPath() throws Exception {
        FileUtils.deleteDirectory(new File("../workdir"));

        final Properties properties = loadPropertiesFromPath("./src/test/resources/de/netpage/sample/database/update/spring", "sample.properties");
        assertEquals(2, properties.keySet().size());
        storePropertiesInPath(properties, "../workdir", "test.properties");
        final File p = new File("../workdir/test.properties");
        assertTrue(p.exists());
        assertTrue(p.isFile());
        assertTrue(p.length() > 0);

        storePropertiesInPath(properties, "../workdir", "test.properties");
    }

    @Test
    public void testLoadPropertiesFromClassPath() throws Exception {
        final Properties properties = loadPropertiesFromClassPath("de/netpage/sample/database/update/spring/sample.properties");
        assertNotNull(properties);
        assertEquals(2, properties.keySet().size());
        assertEquals("value2", properties.get("key2"));
    }

    @Test
    public void testLoadPropertiesFromClassPathError() throws Exception {
        final Properties properties = loadPropertiesFromClassPath("de/netpage/sample/database/update/spring/notfound.properties");
        assertNotNull(properties);
        assertEquals(0, properties.keySet().size());
    }

}