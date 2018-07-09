package io.jenkins.plugins.extlogging.elasticsearch;

import hudson.console.AnnotatedLargeText;
import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class ElasticsearchLogBrowser extends ExternalLogBrowser {

    //TODO: Cache values instead of refreshing them each time
    @Override
    public AnnotatedLargeText overallLog(@Nonnull Run<?, ?> run, boolean b) {
        return new ElasticsearchLogLargeTextProvider(run, null).getLogText();
    }

    @Override
    public AnnotatedLargeText stepLog(@Nonnull Run<?, ?> run, @CheckForNull String stepId, boolean b) {
        return new ElasticsearchLogLargeTextProvider(run, stepId).getLogText();
    }
}
