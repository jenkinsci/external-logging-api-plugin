package io.jenkins.plugins.extlogging.api;

import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.Loggable;

import javax.annotation.Nonnull;

/**
 * Base abstract class for External Log Browsers.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLogBrowser extends LogBrowser {

    public ExternalLogBrowser(@Nonnull Loggable loggable) {
        super(loggable);
    }
}
