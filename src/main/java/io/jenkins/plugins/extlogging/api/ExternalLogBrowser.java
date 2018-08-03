package io.jenkins.plugins.extlogging.api;

import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.Loggable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base abstract class for External Log Browsers.
 * This implementation also implements a convenience method for caching of files.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLogBrowser extends LogBrowser {

    public ExternalLogBrowser(@Nonnull Loggable loggable) {
        super(loggable);
    }

    //TODO: Push warnings to Telemetry API
    //Pipeline: LOGGER.log(Level.WARNING, "Avoid calling getLogFile on " + this,
    //        new UnsupportedOperationException());
    @Override
    public File getLogFile() throws IOException {
        File f = File.createTempFile("deprecated", ".log", getOwner().getTmpDir());
        f.deleteOnExit();
        try (OutputStream os = new FileOutputStream(f)) {
            overallLog().writeRawLogTo(0, os);
        }
        return f;
    }
}
