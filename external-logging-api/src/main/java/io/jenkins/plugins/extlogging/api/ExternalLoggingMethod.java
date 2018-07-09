package io.jenkins.plugins.extlogging.api;

import hudson.console.ConsoleLogFilter;
import hudson.model.Run;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import hudson.model.TaskListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingOutputStream;
import io.jenkins.plugins.extlogging.api.impl.LoggingThroughMasterOutputStreamWrapper;
import jenkins.model.logging.LoggingMethod;

import javax.annotation.CheckForNull;

/**
 * Implements External logging method and simplifies API.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLoggingMethod extends LoggingMethod {

    @Override
    public ConsoleLogFilter createLoggerDecorator(Run<?, ?> run) {
        return new ConsoleLogFilter() {
            @Override
            public OutputStream decorateLogger(Run run, OutputStream logger) throws IOException, InterruptedException {
                return decorateLogger(run, logger);
            }
        };
    }

    public final OutputStream createOutputStream(Run run) {
        final ExternalLoggingEventWriter writer = createWriter(run);
        final List<String> sensitiveStrings = SensitiveStringsProvider.getAllSensitiveStrings(run);
        return ExternalLoggingOutputStream.createOutputStream(writer, sensitiveStrings);
    }

    /**
     * Creates Remotable wrapper.
     * By default, logging happens through master unless there is a custom implementation.
     * @param run Run
     * @return Remotable wrapper
     */
    public OutputStreamWrapper createWrapper(Run run) {
        //TODO: capture agent in API to allow overrides with checks
        return new LoggingThroughMasterOutputStreamWrapper(createOutputStream(run));
    }

    @CheckForNull
    @Override
    public TaskListener createTaskListener(Run<?, ?> run) {
        //TODO: task listener implementation
        return null;
    }

    public abstract ExternalLoggingEventWriter createWriter(Run run);

    public abstract OutputStream decorateLogger(Run run, OutputStream logger);

    @Override
    public OutputStreamWrapper provideOutStream(Run run) {
        return createWrapper(run);
    }

    @Override
    public OutputStreamWrapper provideErrStream(Run run) {
        return createWrapper(run);
    }

}