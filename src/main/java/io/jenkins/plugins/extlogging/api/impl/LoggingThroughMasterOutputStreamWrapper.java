package io.jenkins.plugins.extlogging.api.impl;

import hudson.remoting.RemoteOutputStream;
import jenkins.model.logging.LoggingMethod;

import java.io.OutputStream;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class LoggingThroughMasterOutputStreamWrapper implements LoggingMethod.OutputStreamWrapper {

    final OutputStream ostream;

    public LoggingThroughMasterOutputStreamWrapper(OutputStream stream) {
        this.ostream = stream;
    }

    @Override
    public OutputStream toSerializableOutputStream() {
        return new RemoteOutputStream(ostream);
    }
}
