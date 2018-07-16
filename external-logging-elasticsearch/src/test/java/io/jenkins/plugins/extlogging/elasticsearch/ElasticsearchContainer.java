package io.jenkins.plugins.extlogging.elasticsearch;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import io.jenkins.plugins.extlogging.logstash.LogstashDaoLoggingMethodFactory;
import jenkins.plugins.logstash.LogstashConfiguration;
import jenkins.plugins.logstash.LogstashInstallation;
import jenkins.plugins.logstash.persistence.LogstashIndexerDao;
import org.jenkinsci.test.acceptance.docker.DockerContainer;
import org.jenkinsci.test.acceptance.docker.DockerFixture;
import org.jvnet.hudson.test.JenkinsRule;

import javax.annotation.Nonnull;

/**
 * Elasticsearch test container.
 * @author Oleg Nenashev
 */
@DockerFixture(id = "elasticsearch", ports = 9200)
public class ElasticsearchContainer extends DockerContainer {

    @Nonnull
    public URL getURL() {
        try {
            return new URL("http://" + ipBound(9200) + ":" + port(9200));
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
    }

    public void configureJenkins(JenkinsRule j) throws AssertionError {
        try {
            LogstashInstallation.Descriptor descriptor = LogstashInstallation.getLogstashDescriptor();
            setField(descriptor, "host", getURL().toString());
            setField(descriptor, "port", 9200);
            setField(descriptor, "key", "/logstash/logs");
            setField(descriptor, "type", LogstashIndexerDao.IndexerType.ELASTICSEARCH);

            // TODO: Replace by proper initialization once plugin API is fixed
            // Currently setIndexer() method does not change active indexer.
            LogstashConfiguration cfg = LogstashConfiguration.getInstance();
            Field dataMigrated = cfg.getClass().getDeclaredField("dataMigrated");
            dataMigrated.setAccessible(true);
            dataMigrated.setBoolean(cfg, false);
            LogstashConfiguration.getInstance().migrateData();
        } catch (Exception ex) {
            throw new AssertionError("Failed to configure Logstash Plugin using reflection", ex);
        }

        ExternalLoggingGlobalConfiguration cfg = ExternalLoggingGlobalConfiguration.getInstance();
        cfg.setLogBrowser(new ElasticsearchLogBrowserFactory());
        cfg.setLoggingMethod(new LogstashDaoLoggingMethodFactory());

    }

    private final void setField(LogstashInstallation.Descriptor d, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field dataMigrated = LogstashInstallation.Descriptor.class.getDeclaredField(field);
        dataMigrated.setAccessible(true);
        dataMigrated.set(d, value);
    }
}