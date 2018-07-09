package io.jenkins.plugins.extlogging.api;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.api.util.MockExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowser;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethod;
import jenkins.model.GlobalConfiguration;
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
public class FreestyleJobTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private MockLoggingMethod loggingMethod;

    @Before
    public void setup() throws Exception {
        ExternalLoggingGlobalConfiguration cfg = ExternalLoggingGlobalConfiguration.get();
        loggingMethod = new MockLoggingMethod(tmpDir.newFolder("logs"));
        cfg.setLoggingMethod(loggingMethod);
        cfg.setLogBrowser(new MockLogBrowser());
    }

    @Test
    public void spotcheck() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));

        FreeStyleBuild build = j.buildAndAssertSuccess(project);
        MockExternalLoggingEventWriter writer = loggingMethod.getWriter(build);
        Assert.assertTrue(writer.isEventWritten());
    }

}
