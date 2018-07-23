package io.jenkins.plugins.extlogging.api.util;

import io.jenkins.plugins.extlogging.api.impl.ExternalLoggingGlobalConfiguration;
import org.junit.Before;
import org.junit.rules.TemporaryFolder;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Test util for mock classes.
 * @author Oleg Nenashev
 * @see MockLogBrowser
 * @see MockLoggingMethod
 */
public class MockLoggingTestUtil {

    public static void setup(@Nonnull TemporaryFolder tmpDir) throws Exception {
        ExternalLoggingGlobalConfiguration cfg = ExternalLoggingGlobalConfiguration.getInstance();
        File logDir = tmpDir.newFolder("logs");
        cfg.setLoggingMethod(new MockLoggingMethodFactory(logDir));
        cfg.setLogBrowser(new MockLogBrowserFactory(logDir));
    }
}
