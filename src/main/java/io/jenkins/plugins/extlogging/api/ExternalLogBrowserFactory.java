package io.jenkins.plugins.extlogging.api;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.Loggable;

import javax.annotation.CheckForNull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLogBrowserFactory
        implements Describable<ExternalLogBrowserFactory>, ExtensionPoint {

    @CheckForNull
    public abstract LogBrowser create(Loggable loggable);

    @Override
    public Descriptor<ExternalLogBrowserFactory> getDescriptor() {
        return Jenkins.get().getDescriptor(getClass());
    }

}
