package io.jenkins.plugins.extlogging.api.impl;

import hudson.model.BuildListener;
import io.jenkins.plugins.extlogging.api.ExternalLoggingEventWriter;

import java.io.PrintStream;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class ExternalLoggingBuildListener implements BuildListener {

    private final ExternalLoggingEventWriter writer;

    public ExternalLoggingBuildListener(ExternalLoggingEventWriter writer) {
        this.writer = writer;
    }

    @Override
    public PrintStream getLogger() {
        return new PrintStream(new ExternalLoggingOutputStream(writer));
    }
}
