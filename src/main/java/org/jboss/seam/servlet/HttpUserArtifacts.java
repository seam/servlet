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

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.jboss.seam.servlet.event.qualifier.Created;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.slf4j.Logger;

/**
 * A manager for acquiring HTTP artifacts
 * 
 * @author Nicklas Karlsson
 * 
 */
@RequestScoped
public class HttpUserArtifacts implements Serializable
{
   private static final long serialVersionUID = 5191073522575178427L;

   private HttpSession session;
   private HttpServletRequest request;

   @Inject
   private Logger log;

   protected void requestInitialized(@Observes @Initialized ServletRequestEvent e)
   {
      log.trace("Servlet request initialized with event #0", e);
      request = (HttpServletRequest) e.getServletRequest();
      session = request.getSession();
   }

   protected void requestDestroyed(@Observes @Destroyed ServletRequestEvent e)
   {
      log.trace("Servlet request destroyed with event #0", e);
      request = null;
   }

   protected void sessionInitialized(@Observes @Created HttpSessionEvent e)
   {
      log.trace("HTTP session initalized with event #0", e);
      session = e.getSession();
   }

   protected void sessionDestroyed(@Observes @Destroyed HttpSessionEvent e)
   {
      log.trace("HTTP session destroyed with event #0", e);
      session = null;
   }

   protected HttpSession getSession()
   {
      return session;
   }

   protected HttpServletRequest getRequest()
   {
      return request;
   }

   @Produces
   @HttpParam("")
   protected String getParamValue(InjectionPoint ip)
   {
      return getRequest().getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
   }

}
