package io.jenkins.plugins.extlogging.api;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class Event {

    final String message;
    final long timestamp;

    Map<String, Object> data = new HashMap<>();

    public Event(String message) {
        this(message, System.currentTimeMillis());
    }

    public Event(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
