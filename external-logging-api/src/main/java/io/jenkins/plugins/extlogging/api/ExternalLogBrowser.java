package io.jenkins.plugins.extlogging.api;

import hudson.console.AnnotatedLargeText;
import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import jenkins.model.logging.LogBrowser;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLogBrowser extends LogBrowser {

    public abstract AnnotatedLargeText overallLog(@Nonnull Run<?, ?> run, boolean b);

    public abstract AnnotatedLargeText stepLog(@Nonnull Run<?, ?> run, @CheckForNull String stepId, boolean b);
}
