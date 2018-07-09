package io.jenkins.plugins.extlogging.api;

import hudson.model.Run;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.api.util.MockExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowser;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethod;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class PipelineSmokeTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private MockLoggingMethod loggingMethod;

    @Before
    public void setup() throws Exception {
        ExternalLoggingGlobalConfiguration cfg = ExternalLoggingGlobalConfiguration.getInstance();
        loggingMethod = new MockLoggingMethod(tmpDir.newFolder("logs"));
        cfg.setLoggingMethod(loggingMethod);
        cfg.setLogBrowser(new MockLogBrowser());
    }

    @Test
    public void spotcheck_Master() throws Exception {
        WorkflowJob project = j.createProject(WorkflowJob.class);
        project.setDefinition(new CpsFlowDefinition("echo Hello", true));

        Run build = j.buildAndAssertSuccess(project);
        MockExternalLoggingEventWriter writer = loggingMethod.getWriter(build);
        Assert.assertTrue(writer.isEventWritten());
    }

}
