package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactory;
import jenkins.model.logging.Loggable;

import javax.annotation.CheckForNull;
import java.io.File;
import java.util.HashMap;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLoggingMethodFactory extends ExternalLoggingMethodFactory {

    private File baseDir;

    public MockLoggingMethodFactory(File baseDir) {
        this.baseDir = baseDir;
    }

    @CheckForNull
    @Override
    public ExternalLoggingMethod create(Loggable loggable) {
        return new MockLoggingMethod((Run<?, ?>)loggable, baseDir);
    }
}
