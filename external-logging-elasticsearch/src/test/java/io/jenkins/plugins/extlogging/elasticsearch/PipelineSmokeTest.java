package io.jenkins.plugins.extlogging.elasticsearch;

import hudson.model.Run;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.test.acceptance.docker.DockerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
public class PipelineSmokeTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Rule
    public DockerRule<ElasticsearchContainer> esContainer = new DockerRule<ElasticsearchContainer>(ElasticsearchContainer.class);
    private ElasticsearchContainer container;

    @Before
    public void setup() throws Exception {
        container = esContainer.get();
        container.configureJenkins(j);
    }

    @Test
    public void spotcheck_Default() throws Exception {
        WorkflowJob project = j.createProject(WorkflowJob.class);
        project.setDefinition(new CpsFlowDefinition("echo 'Hello'", true));
        Run build = j.buildAndAssertSuccess(project);
        // Eventual consistency
        //TODO(oleg_nenashev): Probably we need terminator entries in logs
        //to automate handling of such use-cases
        Thread.sleep(1000);
        j.assertLogContains("Hello", build);
    }

}
