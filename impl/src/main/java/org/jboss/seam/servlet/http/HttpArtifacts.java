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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.slf4j.Logger;

/**
 * 
 * @author Nicklas Karlsson
 * 
 *         A source for HTTP artifacts. It observes for and stores the
 *         ServletContext and provides the HttpSession and ServletRequest
 *         objects
 */
@ApplicationScoped
public class HttpArtifacts
{
   private ServletContext servletContext;

   @Inject
   BeanManager beanManager;
   
   @Inject Logger log;

   protected void contextInitialized(@Observes @Initialized ServletContextEvent e)
   {
      log.debug("Servlet context initialized with event #0", e);
      servletContext = e.getServletContext();
      servletContext.setAttribute(BeanManager.class.getName(), beanManager);
   }

   protected void contextDestroyed(@Observes @Destroyed ServletContextEvent e)
   {
      log.debug("Servlet context destroyed with event #0", e);
      servletContext = null;
   }
   
   @Produces
   @ApplicationScoped
   public ServletContext getServletContext()
   {
      return servletContext;
   }

}