package io.jenkins.plugins.extlogging.api.impl;

import hudson.console.AnnotatedLargeText;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import jenkins.model.logging.LogStorage;
import jenkins.model.logging.Loggable;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * External Log Storage implementation.
 * @author Oleg Nenashev
 * @since TODO
 */
@ExportedBean
public class ExternalLogStorage <TLoggable extends Loggable> extends LogStorage<TLoggable> {

    @Nonnull
    private final ExternalLoggingMethod<TLoggable> reporter;
    @Nonnull
    private final ExternalLogBrowser<TLoggable> browser;

    public ExternalLogStorage(TLoggable loggable,
                              @Nonnull ExternalLoggingMethod<TLoggable> reporter,
                              @Nonnull ExternalLogBrowser<TLoggable> browser) {
        super(loggable);
        this.reporter = reporter;
        this.browser = browser;

    }

    @Override
    public void onLoad(@Nonnull TLoggable loggable) {
        super.onLoad(loggable);
        reporter.onLoad(loggable);
        browser.onLoad(loggable);
    }

    @CheckForNull
    @Override
    public TaskListener createTaskListener() throws IOException, InterruptedException {
        return getReporter().createTaskListener();
    }

    @Nonnull
    @Override
    public BuildListener createBuildListener() throws IOException, InterruptedException {
        return getReporter().createBuildListener();
    }

    @Nonnull
    @Override
    public AnnotatedLargeText<TLoggable> overallLog() {
        return getBrowser().overallLog();
    }

    @Nonnull
    @Override
    public AnnotatedLargeText<TLoggable> stepLog(@CheckForNull String s, boolean completed) {
        return getBrowser().stepLog(s, completed);
    }

    @Override
    public InputStream getLogInputStream() throws IOException {
        return getBrowser().getLogInputStream();
    }

    @Override
    public List<String> getLog(int i) throws IOException {
        return getBrowser().getLog(i);
    }

    @Nonnull
    @Override
    public File getLogFile() throws IOException {
        return getBrowser().getLogFile();
    }

    @Override
    public boolean deleteLog() throws IOException {
        return getBrowser().deleteLog();
    }

    @Nonnull
    @Exported
    public ExternalLogBrowser<TLoggable> getBrowser() {
        return browser;
    }

    @Nonnull
    @Exported
    public ExternalLoggingMethod<TLoggable> getReporter() {
        return reporter;
    }



}
