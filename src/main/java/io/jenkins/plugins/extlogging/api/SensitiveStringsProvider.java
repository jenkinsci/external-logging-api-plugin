package io.jenkins.plugins.extlogging.api;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Run;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provides a list of sensitive variables, which should be hidden from console.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class SensitiveStringsProvider implements ExtensionPoint {

    public abstract void getSensitiveStrings(@Nonnull Run<?, ?> run, List<String> dest);

    public static List<String> getAllSensitiveStrings(@Nonnull Run<?, ?> run) {
        final ExtensionList<SensitiveStringsProvider> all = all();
        if (all.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<String> res = new ArrayList<>();
        for (SensitiveStringsProvider provider : all) {
            provider.getSensitiveStrings(run, res);
        }
        return res;
    }

    public static ExtensionList<SensitiveStringsProvider> all() {
        return ExtensionList.lookup(SensitiveStringsProvider.class);
    }
}
