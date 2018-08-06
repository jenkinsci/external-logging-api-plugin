package io.jenkins.plugins.extlogging.api;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import io.jenkins.plugins.extlogging.api.impl.ExternalLogStorage;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.api.util.MockExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.util.MockLogBrowserFactory;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethod;
import io.jenkins.plugins.extlogging.api.util.MockLoggingMethodFactory;
import io.jenkins.plugins.extlogging.api.util.MockLoggingTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class FreestyleJobTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Test
    public void spotcheck_Default() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));

        FreeStyleBuild build = j.buildAndAssertSuccess(project);
        j.assertLogContains("hello", build);
    }

    @Test
    public void spotcheck_Mock() throws Exception {
        MockLoggingTestUtil.setup(tmpDir);
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));

        FreeStyleBuild build = j.buildAndAssertSuccess(project);
        assertThat(build.getLogStorage(), instanceOf(ExternalLogStorage.class));
        ExternalLogStorage storage = (ExternalLogStorage) build.getLogStorage();
        MockLoggingMethod lm = (MockLoggingMethod)storage.getReporter();
        MockExternalLoggingEventWriter writer = lm.getWriter();
        Assert.assertTrue(writer.isEventWritten());
        j.assertLogContains("hello", build);
    }

}
