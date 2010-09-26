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
package org.jboss.seam.servlet.http;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;

/**
 * A manager for acquiring HTTP artifacts
 * 
 * @author Nicklas Karlsson
 * 
 */
@ApplicationScoped
public class HttpServletEnvironmentProducer implements Serializable
{
   private static final long serialVersionUID = 1L;

   private final ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();
   private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();

   @Inject
   private Logger log;

   protected void requestInitialized(@Observes @Initialized final ServletRequestEvent e)
   {
      log.tracev("Servlet request initialized with event #0", e);
      request.set((HttpServletRequest) e.getServletRequest());
      session.set(request.get().getSession());
   }

   protected void requestDestroyed(@Observes @Destroyed final ServletRequestEvent e)
   {
      log.tracev("Servlet request destroyed with event #0", e);
   }

   @Produces
   @RequestScoped
   protected HttpSession getSession()
   {
      return session.get();
   }

   @Produces
   @RequestScoped
   protected HttpServletRequest getRequest()
   {
      return request.get();
   }

}
