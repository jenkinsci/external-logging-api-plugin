package io.jenkins.plugins.extlogging.api.util;

import hudson.console.AnnotatedLargeText;
import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import jenkins.model.logging.Loggable;
import jenkins.model.logging.impl.BrokenAnnotatedLargeText;


import javax.annotation.CheckForNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLogBrowser extends ExternalLogBrowser {

    private static final Logger LOGGER =
            Logger.getLogger(MockLogBrowser.class.getName());

    private File baseDir;

    public MockLogBrowser(Run<?, ?> run, File baseDir) {
        super(run);
        this.baseDir = baseDir;
    }

    @Override
    protected Run<?, ?> getOwner() {
        return (Run<?, ?>)super.getOwner();
    }

    public File getLogFileOrFail(Loggable loggable) throws IOException {
        return new File(baseDir, getOwner().getFullDisplayName() + ".txt");
    }

    @Override
    public AnnotatedLargeText overallLog() {
        final File logFile;
        try {
            logFile = getLogFileOrFail(getOwner());
        } catch (IOException ex) {
            return new BrokenAnnotatedLargeText(ex, getOwner().getCharset());
        }

        return new AnnotatedLargeText<Loggable>
                (logFile, getOwner().getCharset(), getOwner().isLoggingFinished(), getOwner());
    }

    @Override
    public AnnotatedLargeText stepLog(@CheckForNull String stepId, boolean b) {
        // Not supported, there is no default implementation for "step"
        return new BrokenAnnotatedLargeText(
                new UnsupportedOperationException(MockLogBrowser.class.getName() + " does not support partial logs"),
                getOwner().getCharset()
        );
    }

    @Override
    public boolean deleteLog() throws IOException {
        File logFile = getLogFileOrFail(loggable);
        if (logFile.exists()) {
            try {
                Files.delete(logFile.toPath());
            } catch (Exception ex) {
                throw new IOException("Failed to delete " + logFile, ex);
            }
        } else {
            LOGGER.log(Level.FINE, "Trying to delete Log File of {0} which does not exist: {1}",
                    new Object[] {loggable, logFile});
        }
        return true;
    }
}
