package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import jdk.nashorn.internal.objects.Global;
import jenkins.model.GlobalConfiguration;
import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.LoggingMethod;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
@Extension
public class ExternalLoggingGlobalConfiguration extends GlobalConfiguration {

    private static final ExternalLoggingGlobalConfiguration DEFAULT = new DefaultExternalLoggingGlobalConfiguration();

    @CheckForNull
    private LoggingMethod loggingMethod;

    @CheckForNull
    private LogBrowser logBrowser;

    @Nonnull
    public static ExternalLoggingGlobalConfiguration get() {
        ExternalLoggingGlobalConfiguration cfg = GlobalConfiguration.all().get(ExternalLoggingGlobalConfiguration.class);
        return cfg != null ? cfg : DEFAULT;
    }

    public ExternalLoggingGlobalConfiguration() {
        load();
    }

    public void setLogBrowser(@CheckForNull LogBrowser logBrowser) {
        this.logBrowser = logBrowser;
        this.save();
    }

    public void setLoggingMethod(@CheckForNull LoggingMethod loggingMethod) {
        this.loggingMethod = loggingMethod;
        this.save();
    }

    @CheckForNull
    public LogBrowser getLogBrowser() {
        return logBrowser;
    }

    @CheckForNull
    public LoggingMethod getLoggingMethod() {
        return loggingMethod;
    }

    private static final class DefaultExternalLoggingGlobalConfiguration extends ExternalLoggingGlobalConfiguration {

        public DefaultExternalLoggingGlobalConfiguration() {
            // prevent config loading
        }

    }
}
