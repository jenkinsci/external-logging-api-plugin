package io.jenkins.plugins.extlogging.api.util;

import io.jenkins.plugins.extlogging.api.Event;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingEventWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockExternalLoggingEventWriter extends ExternalLoggingEventWriter {

    public File dest;
    private FileWriter writer;

    // Debug flags
    private boolean eventWritten;

    public MockExternalLoggingEventWriter(File dest) throws IOException {
        this.dest = dest;
        this.writer = new FileWriter(dest);
    }

    @Override
    public void writeEvent(Event event) throws IOException {
        eventWritten = true;
        writer.write(event.toStringWithData() + "\n");
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    public File getDest() {
        return dest;
    }

    public boolean isEventWritten() {
        return eventWritten;
    }
}
