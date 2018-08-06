package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import jenkins.model.logging.Loggable;

import java.io.File;

/**
 * Producer for {@link MockLogBrowser}.
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLogBrowserFactory extends ExternalLogBrowserFactory {

    private File baseDir;

    public MockLogBrowserFactory(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public ExternalLogBrowser create(Loggable loggable) {
        return new MockLogBrowser((Run<?, ?>)loggable, baseDir);
    }
}
