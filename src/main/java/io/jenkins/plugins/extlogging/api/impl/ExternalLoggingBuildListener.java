package io.jenkins.plugins.extlogging.api.impl;

import hudson.model.BuildListener;
import io.jenkins.plugins.extlogging.api.ExternalLoggingEventWriter;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class ExternalLoggingBuildListener implements BuildListener {

    protected final ExternalLoggingEventWriter writer;

    public ExternalLoggingBuildListener(ExternalLoggingEventWriter writer) {
        this.writer = writer;
    }

    //TODO: do something better about it
    @Override
    public PrintStream getLogger() {
        try {
            return writer.getLogger();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
