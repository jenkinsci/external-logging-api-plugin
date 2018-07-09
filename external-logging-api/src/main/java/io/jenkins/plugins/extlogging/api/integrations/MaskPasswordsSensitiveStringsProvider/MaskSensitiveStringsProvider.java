package io.jenkins.plugins.extlogging.api.integrations.MaskPasswordsSensitiveStringsProvider;

import com.michelin.cio.hudson.plugins.maskpasswords.MaskPasswordsBuildWrapper;
import com.michelin.cio.hudson.plugins.maskpasswords.MaskPasswordsConfig;
import hudson.Extension;
import hudson.model.BuildableItemWithBuildWrappers;
import hudson.model.Job;
import hudson.model.Run;
import hudson.tasks.BuildWrapper;
import io.jenkins.plugins.extlogging.api.SensitiveStringsProvider;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Oleg Nenashev
 * @since TODO
 */
@Extension(optional = true)
public class MaskSensitiveStringsProvider extends SensitiveStringsProvider {

    private static final Logger LOGGER = Logger.getLogger(MaskSensitiveStringsProvider.class.getName());

    static {
        String wrapperName = MaskPasswordsBuildWrapper.class.getName();
        LOGGER.log(Level.FINEST, "Initialized SensitiveStringsProvider for {0}", wrapperName);
    }

    @Override
    public void getSensitiveStrings(@Nonnull Run<?, ?> run, List<String> dest) {
        for (MaskPasswordsBuildWrapper.VarPasswordPair pair : getVarPasswordPairs(run)) {
            dest.add(pair.getPassword());
        }
    }

    @Restricted(NoExternalUse.class)
    public static List<MaskPasswordsBuildWrapper.VarPasswordPair> getVarPasswordPairs(Run build) {
        List<MaskPasswordsBuildWrapper.VarPasswordPair> allPasswordPairs = new ArrayList<>();
        Job job = build.getParent();
        if (job instanceof BuildableItemWithBuildWrappers) {
            BuildableItemWithBuildWrappers project = (BuildableItemWithBuildWrappers) job;
            for (BuildWrapper wrapper : project.getBuildWrappersList()) {
                if (wrapper instanceof MaskPasswordsBuildWrapper) {
                    MaskPasswordsBuildWrapper maskPasswordsWrapper = (MaskPasswordsBuildWrapper) wrapper;
                    List<MaskPasswordsBuildWrapper.VarPasswordPair> jobPasswordPairs = maskPasswordsWrapper.getVarPasswordPairs();
                    if (jobPasswordPairs != null) {
                        allPasswordPairs.addAll(jobPasswordPairs);
                    }

                    MaskPasswordsConfig config = MaskPasswordsConfig.getInstance();
                    List<MaskPasswordsBuildWrapper.VarPasswordPair> globalPasswordPairs = config.getGlobalVarPasswordPairs();
                    if (globalPasswordPairs != null) {
                        allPasswordPairs.addAll(globalPasswordPairs);
                    }

                    return allPasswordPairs;
                }
            }
        }
        return allPasswordPairs;
    }
}
