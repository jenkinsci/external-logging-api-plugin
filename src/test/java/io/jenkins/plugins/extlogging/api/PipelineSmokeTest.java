package io.jenkins.plugins.extlogging.api;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.api.util.MockExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowser;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethod;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethodFactory;
import io.jenkins.plugins.extlogging.api.util.MockLoggingTestUtil;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class PipelineSmokeTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Test
    public void spotcheck_Default() throws Exception {
        WorkflowJob project = j.createProject(WorkflowJob.class);
        project.setDefinition(new CpsFlowDefinition("echo 'Hello'", true));
        Run build = j.buildAndAssertSuccess(project);
        j.assertLogContains("Hello", build);
    }

    @Test
    public void spotcheck_Mock() throws Exception {
        MockLoggingTestUtil.setup(tmpDir);
        WorkflowJob project = j.createProject(WorkflowJob.class);
        project.setDefinition(new CpsFlowDefinition("echo 'Hello'", true));

        Run build = j.buildAndAssertSuccess(project);
        assertThat(build.getLoggingMethod(), instanceOf(MockLoggingMethod.class));
        MockLoggingMethod loggingMethod = (MockLoggingMethod)build.getLoggingMethod();
        MockExternalLoggingEventWriter writer = loggingMethod.getWriter();
        // Do not try to add it. Pipeline creates separate PipelineLogListeners for each call
        // Assert.assertTrue(writer.isEventWritten());
        j.assertLogContains("Hello", build);
    }

}
