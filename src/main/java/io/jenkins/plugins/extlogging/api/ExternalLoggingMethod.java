package io.jenkins.plugins.extlogging.api;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Node;
import hudson.model.Run;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import hudson.model.TaskListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingBuildListener;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingOutputStream;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingLauncher;
import io.jenkins.plugins.extlogging.api.impl.LoggingThroughMasterOutputStreamWrapper;
import io.jenkins.plugins.extlogging.api.util.UniqueIdHelper;
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
    public BuildListener createBuildListener() throws IOException, InterruptedException {
        return new ExternalLoggingBuildListener(createWriter());
    }

    public final OutputStream createOutputStream() throws IOException, InterruptedException {
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
    public OutputStreamWrapper createWrapper() throws IOException, InterruptedException {
        //TODO: capture agent in API to allow overrides with checks
        return new LoggingThroughMasterOutputStreamWrapper(createOutputStream());
    }

    /**
     * Produces an event writer for the object.
     * This writer is always serializable, so that it can be used on recipient and sender sides.
     * @return Event writer
     */
    @Nonnull
    public final ExternalLoggingEventWriter createWriter() throws IOException, InterruptedException {
        ExternalLoggingEventWriter writer = _createWriter();
        // Produce universal metadata
        writer.addMetadataEntry("jenkinsUrl", Jenkins.get().getRootUrl());
        if (loggable instanceof Run<?, ?>) {
            Run<?, ?> run = (Run<?, ?>) loggable;
            writer.addMetadataEntry("buildNum", run.getNumber());
            writer.addMetadataEntry("jobId", UniqueIdHelper.getOrCreateId(run.getParent()));
        }
        return writer;
    }

    /**
     * Produces a base event writer for the object.
     * This method will be used by {@link #createWriter()}.
     * @return Event writer
     */
    @Nonnull
    protected abstract ExternalLoggingEventWriter _createWriter() throws IOException, InterruptedException;

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
    public OutputStreamWrapper provideRemotableOutStream() throws IOException, InterruptedException {
        return createWrapper();
    }

    /*
     * Produces logging engine for STDERR.
     * It will be used in the {@link ExternalLoggingLauncher}
     * @return Wrapper to be used.
     *         {@code null} will make the wrapper to use the default stream
     */
    @CheckForNull
    public OutputStreamWrapper provideRemotableErrStream() throws IOException, InterruptedException {
        return createWrapper();
    }

}