package io.jenkins.plugins.extlogging.api.util;

import io.jenkins.plugins.extlogging.api.Event;
import io.jenkins.plugins.extlogging.api.ExternalLoggingEventWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * Mock {@link ExternalLoggingEventWriter} for testing purposes.
 * @author Oleg Nenashev
 * @see MockLoggingMethod
 */
public class MockExternalLoggingEventWriter extends ExternalLoggingEventWriter {

    private static final Logger LOGGER =
            Logger.getLogger(MockExternalLoggingEventWriter.class.getName());

    private final File dest;

    // Debug flags
    private boolean eventWritten;

    public MockExternalLoggingEventWriter(File dest, Charset charset) {
        super(charset);
        this.dest = dest;
    }

    @Override
    public void writeEvent(Event event) throws IOException {
        eventWritten = true;
        FileWriter writer = new FileWriter(dest, true);
        writer.write(event.toString() + "\n");
        writer.flush();
        writer.close();
    }

    public File getDest() {
        return dest;
    }

    public boolean isEventWritten() {
        return eventWritten;
    }
}
