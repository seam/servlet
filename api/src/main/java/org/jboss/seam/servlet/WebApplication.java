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
package org.jboss.seam.servlet;

import javax.servlet.ServletContext;

/**
 * Information about the current web application. This object can be used
 * to observe the startup and shutdown events without tying to the servlet API.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
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
