package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import jenkins.model.logging.impl.FileLogBrowser;


import java.io.File;
import java.io.IOException;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class MockLogBrowser extends FileLogBrowser {

    private File baseDir;

    public MockLogBrowser(Run<?, ?> run, File baseDir) {
        super(run);
        this.baseDir = baseDir;
    }

    @Override
    protected Run<?, ?> getOwner() {
        return (Run<?, ?>)super.getOwner();
    }

    @Override
    public File getLogFile() throws IOException {
        return new File(baseDir, getOwner().getFullDisplayName() + ".txt");
    }
}
