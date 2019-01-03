package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactoryDescriptor;
import jenkins.model.logging.Loggable;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Default Disabled implementation.
 * @author Oleg Nenashev
 * @since TODO
 */
public class DisabledExternalLogBrowserFactory extends ExternalLogBrowserFactory {

    @DataBoundConstructor
    public DisabledExternalLogBrowserFactory() {

    }

    @Override
    public ExternalLogBrowser create(Loggable loggable) {
        return null;
    }

    @Extension(ordinal = Float.MAX_VALUE)
    @Symbol("disabled")
    public static class DescriptorImpl extends ExternalLogBrowserFactoryDescriptor {

        @Override
        public String getDisplayName() {
            return "Disabled";
        }
    }
}
