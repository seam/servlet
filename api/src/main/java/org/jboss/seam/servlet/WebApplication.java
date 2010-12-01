package org.jboss.seam.servlet;

import javax.servlet.ServletContext;

/**
 * Information about the current web application. This object can be used
 * to observe the startup and shutdown events without tying to the servlet API.
 * 
 * @author Dan Allen
 */
public class WebApplication
{
   private final String name;
   private final String contextPath;
   private final String serverInfo;
   private final long startTime;

   public WebApplication(ServletContext ctx)
   {
      this(ctx.getServletContextName(), ctx.getContextPath(), ctx.getServerInfo());
   }
   
   public WebApplication(String name, String contextPath, String serverInfo)
   {
      this.name = name;
      this.contextPath = contextPath;
      this.serverInfo = serverInfo;
      this.startTime = System.currentTimeMillis();
   }
   
   public String getName()
   {
      return name;
   }

   public String getContextPath()
   {
      return contextPath;
   }

   public String getServerInfo()
   {
      return serverInfo;
   }

   public long getStartTime()
   {
      return startTime;
   }
   
   public long getRunningTime()
   {
      return System.currentTimeMillis() - startTime;
   }

}
