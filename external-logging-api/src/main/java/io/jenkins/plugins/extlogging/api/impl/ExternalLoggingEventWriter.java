package io.jenkins.plugins.extlogging.api.impl;

import io.jenkins.plugins.extlogging.api.Event;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLoggingEventWriter extends Writer implements Serializable {

    Map<String, Object> metadata = new HashMap<>();

    public abstract void writeEvent(Event event) throws IOException;

    public void writeMessage(String message) throws IOException {
        Event event = new Event(message);
        event.setData(metadata); // We do not copy the entry to save performance, custom implementations may need better logic
        writeEvent(event);
    }

    public void addMetadataEntry(String key, Object value) {
        metadata.put(key, value);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        String message = new String(cbuf, off, len);
        writeMessage(message);
    }

    @Override
    public void close() throws IOException {
        // noop
    }

    @Override
    public void flush() throws IOException {
        // noop
    }
}
