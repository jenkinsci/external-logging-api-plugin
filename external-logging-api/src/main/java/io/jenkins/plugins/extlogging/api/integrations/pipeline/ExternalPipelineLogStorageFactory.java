package io.jenkins.plugins.extlogging.api.integrations.pipeline;

import hudson.Extension;
import hudson.model.Queue;
import hudson.model.Run;
import hudson.model.queue.SubTask;
import io.jenkins.plugins.extlogging.api.ExternalLogBrowser;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import io.jenkins.plugins.extlogging.api.integrations.MaskPasswordsSensitiveStringsProvider.MaskSensitiveStringsProvider;
import jenkins.model.GlobalConfiguration;
import jenkins.model.logging.LogBrowser;
import jenkins.model.logging.LoggingMethod;
import jenkins.model.logging.LoggingMethodLocator;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.log.LogStorage;
import org.jenkinsci.plugins.workflow.log.LogStorageFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
@Extension(optional = true)
public class ExternalPipelineLogStorageFactory implements LogStorageFactory {

    private static final Logger LOGGER = Logger.getLogger(MaskSensitiveStringsProvider.class.getName());

    @CheckForNull
    @Override
    public LogStorage forBuild(@Nonnull FlowExecutionOwner flowExecutionOwner) {
        final WorkflowRun run;
        try {
            final Queue.Executable executable = flowExecutionOwner.getExecutable();
            if (executable instanceof Run) {
                Run r = (Run)executable;
                if (r instanceof WorkflowRun) {
                    run = (WorkflowRun) r;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Failed to locate executable for the execution owner " + flowExecutionOwner, ex);
            return null;
        }

        final LoggingMethod loggingMethod = LoggingMethodLocator.locate(run);
        if (loggingMethod instanceof ExternalLoggingMethod) {
            ExternalLoggingMethod lm = (ExternalLoggingMethod)loggingMethod;

            // TODO: Support configuration, type check
            LogBrowser browser = lm.getDefaulLogBrowser();
            return new ExternalPipelineLogStorage(run, lm, (ExternalLogBrowser) browser);
        }

        return null;
    }
}
