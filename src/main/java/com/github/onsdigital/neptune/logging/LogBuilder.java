package com.github.onsdigital.neptune.logging;

import ch.qos.logback.classic.Level;
import com.github.onsdigital.logging.builder.LogMessageBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class LogBuilder extends LogMessageBuilder {

    public static LogBuilder logBuilder() {
        return new LogBuilder("");
    }

    public LogBuilder(String eventDescription) {
        super(eventDescription);
    }

    public String getLoggerName() {
        return "dp-neptune-spike";
    }

    /**
     * Log at info level.
     *
     * @param message context of the log message.
     */
    public void info(String message) {
        logLevel = Level.INFO;
        description = message;
        log();
    }

    public void error(Throwable t, String message) {
        logLevel = Level.ERROR;
        description = message;
        addParameter("errorContext", message);
        addParameter("class", t.getClass().getName());
        parameters.getParameters().put("stackTrace", ExceptionUtils.getStackTrace(t));
        addMessage(message).log();
    }

}
