package io.jenkins.plugins.extlogging.api;

import hudson.console.ConsoleLogFilter;
import hudson.model.Run;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import hudson.model.TaskListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingOutputStream;
import io.jenkins.plugins.extlogging.api.impl.LoggingThroughMasterOutputStreamWrapper;
import jenkins.model.logging.Loggable;
import jenkins.model.logging.LoggingMethod;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Implements External logging method and simplifies API.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLoggingMethod extends LoggingMethod {

    public ExternalLoggingMethod(@Nonnull Loggable loggable) {
        super(loggable);
    }

    //TODO: imple
    @CheckForNull
    @Override
    public TaskListener createTaskListener() {
        return null;
    }

    @Override
    public ConsoleLogFilter createLoggerDecorator() {
        return new ConsoleLogFilter() {
            @Override
            public OutputStream decorateLogger(Run run, OutputStream logger) throws IOException, InterruptedException {
                return decorateLogger(run, logger);
            }
        };
    }

    private final OutputStream createOutputStream() {
        final ExternalLoggingEventWriter writer = createWriter();

        final List<String> sensitiveStrings;
        if (getOwner() instanceof Run<?, ?>) {
            sensitiveStrings = SensitiveStringsProvider.getAllSensitiveStrings((Run<?, ?>)getOwner());
        } else {
            sensitiveStrings = Collections.emptyList();
        }
        return ExternalLoggingOutputStream.createOutputStream(writer, sensitiveStrings);
    }

    /**
     * Creates Remotable wrapper.
     * By default, logging happens through master unless there is a custom implementation.
     * @return Remotable wrapper
     */
    @CheckForNull
    public OutputStreamWrapper createWrapper() {
        //TODO: capture agent in API to allow overrides with checks
        return new LoggingThroughMasterOutputStreamWrapper(createOutputStream());
    }

    public abstract ExternalLoggingEventWriter createWriter();

    public abstract OutputStream decorateLogger(OutputStream logger);

    @Override
    public OutputStreamWrapper provideRemotableOutStream() {
        return createWrapper();
    }

    @Override
    public OutputStreamWrapper provideRemotableErrStream() {
        return createWrapper();
    }

}