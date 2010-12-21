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
package org.jboss.seam.servlet.exception;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.exception.control.ExceptionToCatch;
import org.jboss.seam.servlet.literal.WebRequestLiteral;
import org.jboss.weld.extensions.beanManager.BeanManagerAware;
import org.jboss.weld.extensions.core.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bridge that forwards unhandled exceptions to the Seam exception handling facility (Seam Catch).
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@Requires("org.jboss.seam.exception.control.extension.CatchExtension")
public class CatchExceptionFilter extends BeanManagerAware implements Filter
{
   private transient Logger log = LoggerFactory.getLogger(CatchExceptionFilter.class);
   
   @Inject
   private BeanManager beanManager;
   
   private boolean enabled;
   
   public void init(FilterConfig config) throws ServletException
   {
      if (beanManager == null)
      {
         try
         {
            beanManager = getBeanManager();
         }
         catch (IllegalStateException e)
         {
            log.info("Could not locate BeanManager. Catch integration for Servlet disabled (even if present on the classpath)");
            return;
         }
      }

      if (!beanManager.getBeans(CatchExceptionFilter.class).isEmpty())
      {
         enabled = true;
         log.info("Catch integration for Servlet enabled");
      }
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      if (!enabled)
      {
         chain.doFilter(request, response);
      }
      else
      {
         try
         {
            chain.doFilter(request, response);
         }
         catch (Exception e)
         {
            ExceptionToCatch catchEvent;
            if (request instanceof HttpServletRequest)
            {
               catchEvent = new ExceptionToCatch(e, WebRequestLiteral.INSTANCE);
//                     new PathLiteral(HttpServletRequest.class.cast(request).getServletPath()));
            }
            else
            {
               catchEvent = new ExceptionToCatch(e, WebRequestLiteral.INSTANCE);
            }
            beanManager.fireEvent(catchEvent);
            // QUESTION should catch handle rethrowing?
            if (!catchEvent.isHandled())
            {
               if (e instanceof ServletException)
               {
                  throw (ServletException) e;
               }
               else if (e instanceof IOException)
               {
                  throw (IOException) e;
               }
               else
               {
                  throw new ServletException(e);
               }
            }
         }
      }
   }

   public void destroy()
   {
   }
}
