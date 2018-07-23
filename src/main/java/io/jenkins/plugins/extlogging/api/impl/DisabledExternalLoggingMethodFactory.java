package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactory;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactoryDescriptor;
import jenkins.model.logging.Loggable;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class DisabledExternalLoggingMethodFactory extends ExternalLoggingMethodFactory {

    @DataBoundConstructor
    public DisabledExternalLoggingMethodFactory() {

    }

    @Override
    public ExternalLoggingMethod create(Loggable loggable) {
        return null;
    }

    @Extension(ordinal = Float.MAX_VALUE)
    @Symbol("disabled")
    public static final class DescriptorImpl extends ExternalLoggingMethodFactoryDescriptor {

        @Override
        public String getDisplayName() {
            return "disabled";
        }
    }
}
