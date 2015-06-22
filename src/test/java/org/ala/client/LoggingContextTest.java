package org.ala.client;

import org.ala.client.util.LoggingContext;
import org.apache.log4j.MDC;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoggingContextTest {

    @Test
    public void settingNullPropertyShouldClearTheEntryFromTheMDC() {
        // prior to this bug fix, the code was throwing a NPE when attempting to set a null value
        LoggingContext.setProperty("bla", "test");

        assertEquals(MDC.get("bla"), "test");

        LoggingContext.setProperty("bla", null);

        assertEquals(MDC.get("bla"), null);
    }
}
