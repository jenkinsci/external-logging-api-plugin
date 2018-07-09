package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import jenkins.model.logging.LoggingMethod;
import jenkins.model.logging.LoggingMethodLocator;

import javax.annotation.CheckForNull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
@Extension
public class ExternalLoggingMethodLocator extends LoggingMethodLocator {

    @CheckForNull
    @Override
    protected LoggingMethod getLoggingMethod(Run run) {
        return ExternalLoggingGlobalConfiguration.get().getLoggingMethod();
    }

}
