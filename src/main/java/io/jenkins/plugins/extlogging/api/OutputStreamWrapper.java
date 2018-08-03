package io.jenkins.plugins.extlogging.api;

import org.jenkinsci.remoting.SerializableOnlyOverRemoting;

import java.io.OutputStream;

public interface OutputStreamWrapper extends SerializableOnlyOverRemoting {

    /**
     * Produces a serializable object which can be sent over the channel
     * @return Serializable output stream, e.g. {@link hudson.remoting.RemoteOutputStream}
     */
    OutputStream toSerializableOutputStream();
}