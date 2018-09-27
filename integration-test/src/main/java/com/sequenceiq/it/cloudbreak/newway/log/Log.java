package com.sequenceiq.it.cloudbreak.newway.log;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.testng.Reporter;

public class Log {
    private Log() {
    }

    public static void logJSON(String message, Object jsonObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, jsonObject);

        Reporter.log(message + writer);
    }

    public static void log(String message, Object... args) {
        log(null, message, args);
    }

    public static void log(Logger logger, String message, Object... args) {
        String format = String.format(message, args);
        log(format);
        if (logger != null) {
            logger.info(format);
        }
    }

    public static void log(String message) {
        Reporter.log(message);
    }
}
