package io.jenkins.plugins.extlogging.api;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.model.logging.Loggable;

import javax.annotation.CheckForNull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLoggingMethodFactory
        implements Describable<ExternalLoggingMethodFactory>, ExtensionPoint {

    @CheckForNull
    public abstract ExternalLoggingMethod create(Loggable loggable);

    @Override
    public Descriptor<ExternalLoggingMethodFactory> getDescriptor() {
        return Jenkins.get().getDescriptor(getClass());
    }

}
