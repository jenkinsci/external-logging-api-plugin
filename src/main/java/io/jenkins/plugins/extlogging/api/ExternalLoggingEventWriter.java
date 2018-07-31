package io.jenkins.plugins.extlogging.api;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

//TODO: jglick is concerned about the event-based logic, maybe we should add a lower-level implementation
//TODO: jglick: "Why do we need events?" in JEP
/**
 * Implements logging of events
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLoggingEventWriter extends Writer implements Serializable {

    Map<String, Serializable> metadata = new HashMap<>();
    AtomicLong messageCounter = new AtomicLong();

    public abstract void writeEvent(Event event) throws IOException;

    public void writeMessage(String message) throws IOException {
        Event event = new Event(messageCounter.getAndIncrement(), message, System.currentTimeMillis());
        event.setData(metadata); // We do not copy the entry to save performance, custom implementations may need better logic
        writeEvent(event);
    }

    //TODO(oleg-nenashev): jglick requests example of complex event
    public void addMetadataEntry(String key, Serializable value) {
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
