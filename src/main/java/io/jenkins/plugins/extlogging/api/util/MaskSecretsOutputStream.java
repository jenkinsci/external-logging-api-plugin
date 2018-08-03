/*
 * The MIT License
 *
 * Copyright (c) 2010-2011, Manufacture Francaise des Pneumatiques Michelin,
 * Romain Seguy
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

package io.jenkins.plugins.extlogging.api.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.console.LineTransformationOutputStream;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

// Copied from the MaskPasswords plugin
/**
 * Custom output stream which masks a predefined set of passwords.
 *
 * @author Romain Seguy (http://openromain.blogspot.com)
 */
public class MaskSecretsOutputStream extends LineTransformationOutputStream {

    private final static String MASKED_PASSWORD = "********";

    private final OutputStream logger;
    private final Pattern passwordsAsPattern;

    /**
     * @param logger The output stream to which this {@link MaskSecretsOutputStream}
     *               will write to
     * @param secrets A collection of {@link String}s to be masked
     * @param regexes A collection of Regular Expression {@link String}s to be masked
     */
    public MaskSecretsOutputStream(OutputStream logger, @CheckForNull Collection<String> secrets, @CheckForNull Collection<String> regexes) {
        this.logger = logger;


        if((secrets != null && secrets.size() > 0) || (regexes != null && regexes.size() > 0)) {
            // passwords are aggregated into a regex which is compiled as a pattern
            // for efficiency
            StringBuilder regex = new StringBuilder().append('(');

            int nbMaskedPasswords = 0;

            if(secrets != null && secrets.size() > 0) {
              for(String password: secrets) {
                  if(StringUtils.isNotEmpty(password)) { // we must not handle empty passwords
                      regex.append(Pattern.quote(password));
                      regex.append('|');
                    try {
                        String encodedPassword = URLEncoder.encode(password, "UTF-8");
                        if (!encodedPassword.equals(password)) {
                            // add to masking regex
                            regex.append(Pattern.quote(encodedPassword));
                            regex.append('|');
                        }
                    } catch (UnsupportedEncodingException e) {
                        // ignore any encoding problem => status quo
                    }
                      nbMaskedPasswords++;
                  }
              }
            }
            if(regexes != null && regexes.size() > 0) {
              for(String user_regex: regexes) {
                  if(StringUtils.isNotEmpty(user_regex)) { // we must not handle empty passwords
                      regex.append(user_regex);
                      regex.append('|');
                      nbMaskedPasswords++;
                  }
              }
            }

            if(nbMaskedPasswords++ >= 1) { // is there at least one password to mask?
                regex.deleteCharAt(regex.length()-1); // removes the last unuseful pipe
                regex.append(')');
                passwordsAsPattern = Pattern.compile(regex.toString());
            }
            else { // no passwords to hide
                passwordsAsPattern = null;
            }
        }
        else { // no passwords to hide
            passwordsAsPattern = null;
        }
    }

    /**
     * @param logger The output stream to which this {@link MaskSecretsOutputStream}
     *               will write to
     * @param passwords A collection of {@link String}s to be masked
     */
    public MaskSecretsOutputStream(OutputStream logger, @CheckForNull Collection<String> passwords) {
        this(logger, passwords, null);
    }

    // TODO: The logic relies on the default encoding, which may cause issues when master and agent have different encodings
    @SuppressFBWarnings(value = "DM_DEFAULT_ENCODING", justification = "Open TODO item for wider rework")
    @Override
    protected void eol(byte[] bytes, int len) throws IOException {
        String line = new String(bytes, 0, len);
        if(passwordsAsPattern != null) {
            line = passwordsAsPattern.matcher(line).replaceAll(MASKED_PASSWORD);
        }
        logger.write(line.getBytes());
    }

    /**
     * {@inheritDoc}
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
        logger.close();
    }

    /**
     * {@inheritDoc}
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        super.flush();
        logger.flush();
    }
}