package io.jenkins.plugins.extlogging.api.impl;

import hudson.Extension;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethodFactory;
import jenkins.model.logging.LogStorage;
import jenkins.model.logging.LogStorageFactory;
import jenkins.model.logging.Loggable;

import javax.annotation.CheckForNull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Locator which provides logging methods and browsers from {@link ExternalLoggingGlobalConfiguration}.
 * @author Oleg Nenashev
 * @see ExternalLoggingGlobalConfiguration
 */
@Extension
public class ExternalLogStorageFactory extends LogStorageFactory {

    private static final Logger LOGGER =
            Logger.getLogger(ExternalLogStorageFactory.class.getName());

    @CheckForNull
    @Override
    protected LogStorage getLogStorage(Loggable loggable) {
        ExternalLoggingMethod method = getLoggingMethod(loggable);
        ExternalLogBrowser browser = getLogBrowser(loggable);
        if (method == null || browser == null) {
            LOGGER.log(Level.FINE, "Cannot find external log reporter or browser for {0}. " +
                    "Reporter: {1}, browser: {2}",
                    new Object[] {loggable, method, browser});
            return null;
        }

        return new ExternalLogStorage(loggable, method, browser);
    }

    @CheckForNull
    protected ExternalLoggingMethod getLoggingMethod(Loggable loggable) {
        ExternalLoggingMethodFactory factory = ExternalLoggingGlobalConfiguration.getInstance().getLoggingMethod();
        if (factory != null) {
            return factory.create(loggable);
        }
        return null;
    }

    @CheckForNull
    protected ExternalLogBrowser getLogBrowser(Loggable loggable) {
        ExternalLogBrowser browser = null;
        ExternalLogBrowserFactory factory = ExternalLoggingGlobalConfiguration.getInstance().getLogBrowser();
        if (factory != null) {
            browser = factory.create(loggable);
        }

        // If factories cannot provide the implementation, try the default
        if (browser == null) {
            ExternalLoggingMethod method = getLoggingMethod(loggable);
            if (method != null) {
                browser = method.getDefaultLogBrowser();
            }
        }

        return browser;
    }
}
