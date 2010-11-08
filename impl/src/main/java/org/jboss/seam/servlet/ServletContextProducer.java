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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jboss.seam.servlet.beanManager.ServletContextBeanManagerProvider;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A source for HTTP artifacts. It observes for and stores the ServletContext
 * and provides the HttpSession and ServletRequest objects
 *
 * @author Nicklas Karlsson
 */
@ApplicationScoped
public class ServletContextProducer
{
   private ServletContext servletContext;

   @Inject
   private BeanManager beanManager;

   private Logger log = LoggerFactory.getLogger(ServletContextProducer.class);

   protected void contextInitialized(@Observes @Initialized final ServletContext ctx)
   {
      log.debug("Servlet context initialized: " + ctx);
      ctx.setAttribute(BeanManager.class.getName(), beanManager);
      ServletContextBeanManagerProvider.setServletContext(ctx);
      servletContext = ctx;
   }

   protected void contextDestroyed(@Observes @Destroyed final ServletContext ctx)
   {
      log.debug("Servlet context destroyed");
      servletContext = null;
   }

   @Produces
   @ApplicationScoped
   public ServletContext getServletContext()
   {
      return servletContext;
   }
}
