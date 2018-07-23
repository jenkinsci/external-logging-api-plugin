package io.jenkins.plugins.extlogging.api;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Node;
import hudson.model.Run;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import hudson.model.TaskListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingBuildListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingOutputStream;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingLauncher;
import io.jenkins.plugins.extlogging.api.impl.LoggingThroughMasterOutputStreamWrapper;
import jenkins.model.Jenkins;
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

    //TODO: implement
    @CheckForNull
    @Override
    public TaskListener createTaskListener() {
        return null;
    }

    // TODO: Implement event-based logic instead of the
    @Override
    public BuildListener createBuildListener() {
        return new ExternalLoggingBuildListener(createWriter());
    }

    public final OutputStream createOutputStream() {
        final ExternalLoggingEventWriter writer = createWriter();

        final List<String> sensitiveStrings;
        if (getOwner() instanceof Run<?, ?>) {
            sensitiveStrings = SensitiveStringsProvider.getAllSensitiveStrings((Run<?, ?>)getOwner());
        } else {
            sensitiveStrings = Collections.emptyList();
        }
        return ExternalLoggingOutputStream.createOutputStream(writer, sensitiveStrings);
    }

    //TODO: document null
    /**
     * Creates Remotable wrapper.
     * By default, logging happens through master unless there is a custom implementation defined.
     * @return Remotable wrapper
     */
    @CheckForNull
    public OutputStreamWrapper createWrapper() {
        //TODO: capture agent in API to allow overrides with checks
        return new LoggingThroughMasterOutputStreamWrapper(createOutputStream());
    }

    public abstract ExternalLoggingEventWriter createWriter();

    public abstract OutputStream decorateLogger(OutputStream logger);

    @Nonnull
    @Override
    public Launcher decorateLauncher(@Nonnull Launcher original,
                                     @Nonnull Run<?,?> run, @Nonnull Node node) {
        if (node instanceof Jenkins) {
            return new ExternalLoggingLauncher.DefaultLocalLauncher(original);
        } else {
            return new ExternalLoggingLauncher.DefaultRemoteLauncher(original, this);
        }
    }

    /**
     * Produces logging engine for STDOUT.
     * It will be used in the {@link ExternalLoggingLauncher}
     * @return Wrapper to be used.
     *         {@code null} will make the wrapper to use the default stream
     */
    @CheckForNull
    public OutputStreamWrapper provideRemotableOutStream() {
        return createWrapper();
    }

    /*
     * Produces logging engine for STDERR.
     * It will be used in the {@link ExternalLoggingLauncher}
     * @return Wrapper to be used.
     *         {@code null} will make the wrapper to use the default stream
     */
    @CheckForNull
    public OutputStreamWrapper provideRemotableErrStream() {
        return createWrapper();
    }

}