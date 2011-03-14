/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.servlet.log;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.logging.Logger.Level;
import org.jboss.seam.solder.logging.LogMessage;
import org.jboss.seam.solder.logging.Message;
import org.jboss.seam.solder.logging.MessageLogger;

/**
 * Type-safe log messages for the Seam Servlet module
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@MessageLogger
public interface ServletLog {
    static String CATEGORY = "seam-servlet";

    @LogMessage(level = Level.TRACE)
    @Message("Servlet context initialized: %s")
    void servletContextInitialized(ServletContext ctx);

    @LogMessage(level = Level.TRACE)
    @Message("Servlet context destroyed: %s")
    void servletContextDestroyed(ServletContext ctx);

    @LogMessage(level = Level.TRACE)
    @Message("Servlet request initialized: %s")
    void servletRequestInitialized(ServletRequest request);

    @LogMessage(level = Level.TRACE)
    @Message("Servlet request destroyed: %s")
    void servletRequestDestroyed(ServletRequest request);

    @LogMessage(level = Level.TRACE)
    @Message("Servlet response initialized: %s")
    void servletResponseInitialized(ServletResponse response);

    @LogMessage(level = Level.TRACE)
    @Message("Servlet response destroyed: %s")
    void servletResponseDestroyed(ServletResponse response);

    @LogMessage(level = Level.INFO)
    @Message("CDI BeanManager cannot be found. Not sending event %s with qualifiers %s")
    void skippingEventNoBeanManager(Object payload, @SuppressWarnings("rawtypes") List qualifiers);

    @LogMessage(level = Level.INFO)
    @Message("Catch Integration for Servlets enabled")
    void catchIntegrationEnabled();

    @LogMessage(level = Level.INFO)
    @Message("Could not locate CDI BeanManager. Catch Integration for Servlets disabled")
    void catchIntegrationDisabledNoBeanManager();
}
