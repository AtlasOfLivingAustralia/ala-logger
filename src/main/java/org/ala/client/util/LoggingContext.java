package org.ala.client.util;


import org.apache.log4j.MDC;

public class LoggingContext {

    private LoggingContext() {
    }

    public static void setProperty(String propertyName, String value) {
        MDC.put(propertyName, value);
    }

    public static void clearContext() {
        MDC.clear();
    }
}
