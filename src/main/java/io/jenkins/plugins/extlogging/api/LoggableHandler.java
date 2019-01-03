/*
 * The MIT License
 *
 * Copyright 2018 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jenkins.plugins.extlogging.api;

import jenkins.model.logging.Loggable;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.Beta;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Object which operates with {@link Loggable} items.
 * @param <TLoggable> Loggable type
 * @author Oleg Nenashev
 * @since TODO
 */
@ExportedBean
@Restricted(Beta.class)
public abstract class LoggableHandler <TLoggable extends Loggable> {

    protected transient TLoggable loggable;

    public LoggableHandler(@Nonnull TLoggable loggable) {
        this.loggable = loggable;
    }

    @Exported
    public String getId() {
        return getClass().getName();
    }

    /**
     * Called when the owner is loaded from disk.
     * The owner may be persisted on the disk, so the build reference should be {@code transient} (quasi-{@code final}) and restored here.
     * @param loggable an owner to which this component is associated.
     */
    public void onLoad(@Nonnull TLoggable loggable) {
        this.loggable = loggable;
    }

    public static <T extends Loggable> void onLoad(@Nonnull T loggable, @CheckForNull LoggableHandler<T> logHandler) {
        if (logHandler != null) {
            logHandler.onLoad(loggable);
        }
    }

    @Nonnull
    protected TLoggable getOwner() {
        if (loggable == null) {
            throw new IllegalStateException("Owner has not been assigned to the object yet");
        }
        return loggable;

    }
}
