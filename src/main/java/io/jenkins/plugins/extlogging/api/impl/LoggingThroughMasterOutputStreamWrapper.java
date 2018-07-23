package io.jenkins.plugins.extlogging.api.impl;

import hudson.remoting.RemoteOutputStream;
import io.jenkins.plugins.extlogging.api.OutputStreamWrapper;

import java.io.OutputStream;

/**
 * Default {@link OutputStreamWrapper} implementation, which sends logs through the master.
 * This wrapper can be used, when {@link io.jenkins.plugins.extlogging.api.ExternalLoggingMethod}
 * does not support logging from agents.
 * @author Oleg Nenashev
 * @since TODO
 */
public class LoggingThroughMasterOutputStreamWrapper implements OutputStreamWrapper {

    private final OutputStream ostream;

    public LoggingThroughMasterOutputStreamWrapper(OutputStream stream) {
        this.ostream = stream;
    }

    @Override
    public OutputStream toSerializableOutputStream() {
        return new RemoteOutputStream(ostream);
    }
}
