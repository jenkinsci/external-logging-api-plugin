package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactory;
import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.Loggable;
import jenkins.model.logging.LoggingMethod;
import jenkins.model.logging.LoggingMethodLocator;

import javax.annotation.CheckForNull;

/**
 * Locator which provides logging nethods and browsers from {@link ExternalLoggingGlobalConfiguration}.
 * @author Oleg Nenashev
 * @see ExternalLoggingGlobalConfiguration
 */
@Extension
public class ExternalLoggingMethodLocator extends LoggingMethodLocator {

    @CheckForNull
    @Override
    protected LoggingMethod getLoggingMethod(Loggable loggable) {
        ExternalLoggingMethodFactory factory = ExternalLoggingGlobalConfiguration.getInstance().getLoggingMethod();
        if (factory != null) {
            return factory.create(loggable);
        }
        return null;
    }

    @CheckForNull
    @Override
    protected LogBrowser getLogBrowser(Loggable loggable) {
        ExternalLogBrowserFactory factory = ExternalLoggingGlobalConfiguration.getInstance().getLogBrowser();
        if (factory != null) {
            return factory.create(loggable);
        }
        return null;
    }
}
