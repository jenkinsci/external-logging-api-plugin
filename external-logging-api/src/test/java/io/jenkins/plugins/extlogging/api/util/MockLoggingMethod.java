package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import jenkins.model.logging.LogBrowser;

import javax.annotation.CheckForNull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLoggingMethod extends ExternalLoggingMethod {

    File baseDir;
    transient HashMap<Run, MockExternalLoggingEventWriter> writers;

    public MockLoggingMethod(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public ExternalLoggingEventWriter createWriter(Run run) {
        final MockExternalLoggingEventWriter writer;
        try {
            writer = new MockExternalLoggingEventWriter(new File(baseDir, run.getFullDisplayName() + ".txt"));
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        writers.put(run, writer);
        return writer;
    }

    public HashMap<Run, MockExternalLoggingEventWriter> getWriters() {
        return writers;
    }

    public MockExternalLoggingEventWriter getWriter(Run run) {
        return writers.get(run);
    }

    @Override
    public OutputStream decorateLogger(Run run, OutputStream logger) {
        throw new AssertionError("Not Implemented");
    }

    @CheckForNull
    @Override
    public LogBrowser getDefaulLogBrowser() {
        return new MockLogBrowser();
    }
}
