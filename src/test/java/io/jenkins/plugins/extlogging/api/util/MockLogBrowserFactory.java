package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.Loggable;

import java.io.File;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLogBrowserFactory extends ExternalLogBrowserFactory {

    private File baseDir;

    public MockLogBrowserFactory(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public LogBrowser create(Loggable loggable) {
        return new MockLogBrowser((Run<?, ?>)loggable, baseDir);
    }
}
