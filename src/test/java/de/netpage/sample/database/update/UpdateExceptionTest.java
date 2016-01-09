package de.netpage.sample.database.update;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Testfälle für UpdateException.
 */
public class UpdateExceptionTest {

    @Test
    public void testCreate() throws Exception {
        assertNotNull(new UpdateException("Test"));
        assertNotNull(new UpdateException("Test", new Exception()));
    }

}