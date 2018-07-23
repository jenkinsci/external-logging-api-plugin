package io.jenkins.plugins.extlogging.api.impl;

import hudson.console.LineTransformationOutputStream;
import io.jenkins.plugins.extlogging.api.util.MaskSecretsOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class ExternalLoggingOutputStream extends LineTransformationOutputStream {

    ExternalLoggingEventWriter writer;

    public ExternalLoggingOutputStream(ExternalLoggingEventWriter writer) {
        this.writer = writer;
    }

    @Override protected void eol(byte[] b, int len) throws IOException {
        int eol = len;
        while (eol > 0) {
            byte c = b[eol - 1];
            if (c == '\n' || c == '\r') {
                eol--;
            } else {
                break;
            }
        }
        String message = new String(b, 0, eol, StandardCharsets.UTF_8);
        writer.writeMessage(message);
    }

    public static OutputStream createOutputStream(ExternalLoggingEventWriter writer, Collection<String> sensitiveStrings) {
        if (sensitiveStrings.isEmpty()) {
            return new ExternalLoggingOutputStream(writer);
        } else {
            return new MaskSecretsOutputStream(
                    new ExternalLoggingOutputStream(writer),
                    sensitiveStrings,
                    Collections.emptyList()
            );
        }
    }
}
