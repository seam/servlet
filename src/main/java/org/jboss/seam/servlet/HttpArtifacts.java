/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.servlet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.servlet.event.qualifier.Initialized;

/**
 * 
 * @author Nicklas Karlsson
 * 
 *         A source for HTTP artifacts. It observes for and stores the
 *         ServletContext, HttpServletRequest and HttpSession objects. It also
 *         produces the request values for @HttpParam
 */
@ApplicationScoped
public class HttpArtifacts
{
   private ServletContext servletContext;

   @Inject
   HttpUserArtifacts httpUserArtifacts;

   @Inject
   BeanManager beanManager;

   protected void pickup(@Observes @Initialized ServletContextEvent e)
   {
      servletContext = e.getServletContext();
      servletContext.setAttribute(BeanManager.class.getName(), beanManager);
   }

   /**
    * Gets the current servlet context
    * 
    * @throws IllegalStateException if the servlet context has not been set
    * @return The servlet context
    */
   public ServletContext getServletContext()
   {
      if (servletContext == null)
      {
         throw new IllegalStateException("Servlet Context is not set");
      }
      return servletContext;
   }

   /**
    * Gets the current HTTP servlet request
    * 
    * @throws IllegalStateException if the request has not been set
    * @return the request
    */
   public HttpServletRequest getRequest()
   {
      HttpServletRequest request = httpUserArtifacts.getRequest();
      if (request == null)
      {
         throw new IllegalStateException("HTTP servlet request is not set");
      }
      return request;
   }

   /**
    * Gets the current HTTP session
    * 
    * @throws IllegalStateException if the session has not been set
    * @return the session
    */
   public HttpSession getSession()
   {
      HttpSession session = httpUserArtifacts.getSession();
      if (session == null)
      {
         throw new IllegalStateException("HTTP session is not set");
      }
      return session;
   }
}