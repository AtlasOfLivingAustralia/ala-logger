package org.ala.client.util;


import org.apache.log4j.MDC;

public class LoggingContext {

    private LoggingContext() {
    }

    public static void setProperty(String propertyName, String value) {
        // MDC throws a NullPointerException if you attempt to set a null property value (uses Hashtable under the hood)
        if (value == null) {
            MDC.remove(propertyName);
        } else {
            MDC.put(propertyName, value);
        }
    }

    public static void clearContext() {
        MDC.clear();
    }
}
