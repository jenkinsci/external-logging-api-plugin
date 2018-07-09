package io.jenkins.plugins.extlogging.logstash;

import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.OutputStream;
import java.util.List;

import io.jenkins.plugins.extlogging.api.ExternalLoggingEventWriter;
import io.jenkins.plugins.extlogging.api.ExternalLoggingMethod;
import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingOutputStream;
import io.jenkins.plugins.extlogging.elasticsearch.ElasticsearchLogBrowser;
import jenkins.model.logging.LogBrowser;
import jenkins.plugins.logstash.LogstashConfiguration;
import jenkins.plugins.logstash.persistence.ElasticSearchDao;
import jenkins.plugins.logstash.persistence.LogstashIndexerDao;

import javax.annotation.CheckForNull;

/**
 * Perform logging to {@link LogstashIndexerDao}.
 *
 * @author Oleg Nenashev
 */
public class LogstashDaoLoggingMethod extends ExternalLoggingMethod {

    String prefix;

    @CheckForNull
    @Override
    public LogBrowser getDefaulLogBrowser() {
        LogstashIndexerDao dao = LogstashConfiguration.getInstance().getIndexerInstance();
        if (dao instanceof ElasticSearchDao) {
            return new ElasticsearchLogBrowser();
        }
        return null;
    }

    @Override
    public ExternalLoggingEventWriter createWriter(Run run) {
        LogstashIndexerDao dao = LogstashConfiguration.getInstance().getIndexerInstance();
        return new RemoteLogstashWriter(run, TaskListener.NULL, prefix, dao);
    }

    @Override
    public OutputStream decorateLogger(Run run, OutputStream logger) {
      //  LogstashWriter logstash = new LogstashWriter(run, TaskListener.NULL, logger, prefix);
      //  RemoteLogstashOutputStream los = new RemoteLogstashOutputStream(logstash, "prefix");
      //  return los.maskPasswords(SensitiveStringsProvider.getAllSensitiveStrings(run));
        // TODO: implement
        return null;
    }

    private static class LogstashOutputStreamWrapper implements OutputStreamWrapper {

        private final RemoteLogstashWriter wr;
        private final List<String> passwordStrings;

        public LogstashOutputStreamWrapper(RemoteLogstashWriter wr, List<String> passwordStrings, String prefix) {
            this.wr = wr;
            this.passwordStrings = passwordStrings;
        }

        public Object readResolve() {
            return ExternalLoggingOutputStream.createOutputStream(wr, passwordStrings);
        }

        @Override
        public OutputStream toRawOutputStream() {
            return toSerializableOutputStream();
        }

        @Override
        public OutputStream toSerializableOutputStream() {
            return ExternalLoggingOutputStream.createOutputStream(wr, passwordStrings);
        }
    }

}