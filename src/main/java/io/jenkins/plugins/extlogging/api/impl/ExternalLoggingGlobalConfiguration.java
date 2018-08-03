package io.jenkins.plugins.extlogging.api.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactory;
import jenkins.model.GlobalConfiguration;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

//TODO: support global configuration
/**
 * @author Oleg Nenashev
 * @since TODO
 */
@Extension
@Symbol("externalLogging")
public class ExternalLoggingGlobalConfiguration extends GlobalConfiguration {

    private static final ExternalLoggingGlobalConfiguration DEFAULT = new DefaultExternalLoggingGlobalConfiguration();

    @CheckForNull
    private ExternalLoggingMethodFactory loggingMethod;

    @CheckForNull
    private ExternalLogBrowserFactory logBrowser;

    @Nonnull
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE",
                        justification = "Findbugs does not handle asserts right")
    public static ExternalLoggingGlobalConfiguration getInstance() {
        ExternalLoggingGlobalConfiguration cfg = GlobalConfiguration.all().get(ExternalLoggingGlobalConfiguration.class);
        assert cfg != null : "Global configuration should be present";
        return cfg != null ? cfg : DEFAULT;
    }

    public ExternalLoggingGlobalConfiguration() {
        load();
    }

    @DataBoundSetter
    public void setLogBrowser(@CheckForNull ExternalLogBrowserFactory logBrowser) {
        this.logBrowser = logBrowser;
        this.save();
    }

    @DataBoundSetter
    public void setLoggingMethod(@CheckForNull ExternalLoggingMethodFactory loggingMethod) {
        this.loggingMethod = loggingMethod;
        this.save();
    }

    @CheckForNull
    public ExternalLogBrowserFactory getLogBrowser() {
        return logBrowser;
    }

    @CheckForNull
    public ExternalLoggingMethodFactory getLoggingMethod() {
        return loggingMethod;
    }

    private static final class DefaultExternalLoggingGlobalConfiguration extends ExternalLoggingGlobalConfiguration {

        public DefaultExternalLoggingGlobalConfiguration() {
            // prevent config loading
        }

    }
}
