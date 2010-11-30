package org.jboss.seam.servlet.log;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

@MessageLogger
public interface ServletLog
{
   @LogMessage(level = Level.TRACE) @Message("Servlet context initialized: %s")
   void servletContextInitialized(ServletContext ctx);
   
   @LogMessage(level = Level.TRACE) @Message("Servlet context destroyed: %s")
   void servletContextDestroyed(ServletContext ctx);
   
   @LogMessage(level = Level.TRACE) @Message("Servlet request initialized: %s")
   void servletRequestInitialized(HttpServletRequest request);
   
   @LogMessage(level = Level.TRACE) @Message("Servlet request destroyed: %s")
   void servletRequestDestroyed(HttpServletRequest request);
}
