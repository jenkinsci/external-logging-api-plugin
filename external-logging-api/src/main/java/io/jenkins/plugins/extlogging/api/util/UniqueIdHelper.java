package io.jenkins.plugins.extlogging.api.util;

import hudson.model.Run;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.uniqueid.IdStore;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

/**
 * Creates on-demand Unique IDs.
 * @author Oleg Nenashev
 */
public class UniqueIdHelper {

    @CheckForNull
    public static String getOrCreateId(@Nonnull Run<?,?> run) {
        return getOrCreateId(run.getParent());
    }

    @CheckForNull
    public static String getOrCreateId(@Nonnull hudson.model.Job<?,?> job) {
        if (Jenkins.getInstance() == null) {
            return null;
        }

        String id = IdStore.getId(job);
        if (id == null) {
            IdStore.makeId(job);
            id = IdStore.getId(job);;
        }
        return id;
    }
}
