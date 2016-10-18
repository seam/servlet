package org.jboss.seam.servlet.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.seam.servlet.event.Initialized;

/**
 * @author Dan Allen
 */
@ApplicationScoped
public class CharacterEncodingConfig {
    private String encoding;

    private boolean override = false;

    protected void apply(@Observes @Initialized ServletResponse response, ServletRequest request) throws Exception {
        if (encoding != null && (override || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(encoding);
            if (override) {
                response.setCharacterEncoding(encoding);
            }
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
