package io.jenkins.plugins.extlogging.api;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.api.util.MockExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethod;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethodFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class FreestyleJobTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private MockLoggingMethodFactory loggingMethodFactory;

    @Before
    public void setup() throws Exception {
        ExternalLoggingGlobalConfiguration cfg = ExternalLoggingGlobalConfiguration.getInstance();
        File logDir = tmpDir.newFolder("logs");
        loggingMethodFactory = new MockLoggingMethodFactory(logDir);
        cfg.setLoggingMethod(loggingMethodFactory);
        cfg.setLogBrowser(new MockLogBrowserFactory(logDir));
    }

    @Test
    public void spotcheck() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));

        FreeStyleBuild build = j.buildAndAssertSuccess(project);
        MockLoggingMethod lm = (MockLoggingMethod)build.getLoggingMethod();
        MockExternalLoggingEventWriter writer = lm.getWriter();
        Assert.assertTrue(writer.isEventWritten());
    }

}
