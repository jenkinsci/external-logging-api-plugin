package io.jenkins.plugins.extlogging.api;

import hudson.console.AnnotatedLargeText;
import hudson.console.ConsoleNote;
import hudson.model.Run;
import jenkins.model.logging.Loggable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * Base abstract class for External Log Browsers.
 * This implementation also implements a convenience method for caching of files.
 * @author Oleg Nenashev
 * @since TODO
 */
public abstract class ExternalLogBrowser<T extends Loggable> extends LoggableHandler {

    public ExternalLogBrowser(@Nonnull Loggable loggable) {
        super(loggable);
    }

    //TODO: Push warnings to Telemetry API
    //Pipeline: LOGGER.log(Level.WARNING, "Avoid calling getLogFile on " + this,
    //        new UnsupportedOperationException());
    /**
     * Gets log as a file.
     * This is a compatibility method, which is used in {@link Run#getLogFile()}.
     * {@link ExternalLogBrowser} implementations may provide it, e.g. by creating temporary files if needed.
     * @return Log file. If it does not exist, {@link IOException} should be thrown
     * @throws IOException Log file cannot be retrieved
     * @deprecated The method is available for compatibility purposes only
     */
    public File getLogFile() throws IOException {
        File f = File.createTempFile("deprecated", ".log", getOwner().getTmpDir());
        f.deleteOnExit();
        try (OutputStream os = new FileOutputStream(f)) {
            overallLog().writeRawLogTo(0, os);
        }
        return f;
    }

    /**
     * Gets log for an object.
     * @return Created log or {@link jenkins.model.logging.impl.BrokenAnnotatedLargeText} if it cannot be retrieved
     */
    @Nonnull
    public abstract AnnotatedLargeText<T> overallLog();

    //TODO: jglick requests justification of why it needs to be in the core
    /**
     * Gets log for a part of the object.
     * @param stepId Identifier of the step to be displayed.
     *               It may be Pipeline step or other similar abstraction
     * @param completed indicates that the step is completed
     * @return Created log or {@link jenkins.model.logging.impl.BrokenAnnotatedLargeText} if it cannot be retrieved
     */
    @Nonnull
    public abstract AnnotatedLargeText<T> stepLog(@CheckForNull String stepId, boolean completed);

    public InputStream getLogInputStream() throws IOException {
        // Inefficient but probably rarely used anyway.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        overallLog().writeRawLogTo(0, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public Reader getLogReader() throws IOException {
        // As above.
        return overallLog().readAll();
    }

    @SuppressWarnings("deprecation")
    public String getLog() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        overallLog().writeRawLogTo(0, baos);
        return baos.toString(loggable.getCharset().name());
    }

    public List<String> getLog(int maxLines) throws IOException {
        int lineCount = 0;
        List<String> logLines = new LinkedList<>();
        if (maxLines == 0) {
            return logLines;
        }
        try (BufferedReader reader = new BufferedReader(getLogReader())) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                logLines.add(line);
                ++lineCount;
                if (lineCount > maxLines) {
                    logLines.remove(0);
                }
            }
        }
        if (lineCount > maxLines) {
            logLines.set(0, "[...truncated " + (lineCount - (maxLines - 1)) + " lines...]");
        }
        return ConsoleNote.removeNotes(logLines);
    }

    /**
     * Deletes the log in the storage.
     * @return {@code true} if the log was deleted.
     *         {@code false} if Log deletion is not supported.
     * @throws IOException Failed to delete the log.
     */
    public abstract boolean deleteLog() throws IOException;


}
