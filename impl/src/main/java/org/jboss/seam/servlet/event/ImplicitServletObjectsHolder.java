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
package org.jboss.seam.servlet.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.seam.servlet.beanManager.ServletContextAttributeProvider;
import org.jboss.seam.servlet.log.ServletLog;
import org.jboss.weld.extensions.log.Category;

/**
 * A manager for tracking the contextual Servlet objects, specifically the
 * {@link ServletContext}, {@link HttpServletRequest} and {@link HttpServletResponse}.
 *  
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@ApplicationScoped
public class ImplicitServletObjectsHolder
{
   @Inject @Category("seam-servlet")
   private ServletLog log;
   
   private ServletContext servletContext;
   
   private final ThreadLocal<ServletRequest> request = new ThreadLocal<ServletRequest>()
   {
      @Override
      protected ServletRequest initialValue()
      {
         return null;
      }
   };
   
   private final ThreadLocal<ServletResponse> response = new ThreadLocal<ServletResponse>()
   {
      @Override
      protected ServletResponse initialValue()
      {
         return null;
      }
   };
   
   protected void contextInitialized(@Observes @Initialized final InternalServletContextEvent e, BeanManager beanManager)
   {
      ServletContext ctx = e.getServletContext();
      log.servletContextInitialized(ctx);
      ctx.setAttribute(BeanManager.class.getName(), beanManager);
      ServletContextAttributeProvider.setServletContext(ctx);
      servletContext = ctx;
   }

   protected void contextDestroyed(@Observes @Destroyed final InternalServletContextEvent e)
   {
      log.servletContextDestroyed(e.getServletContext());
      servletContext = null;
   }
   
   protected void requestInitialized(@Observes @Initialized final InternalServletRequestEvent e)
   {
      ServletRequest req = e.getServletRequest();
      log.servletRequestInitialized(req);
      request.set(req);
   }

   protected void requestDestroyed(@Observes @Destroyed final InternalServletRequestEvent e)
   {
      log.servletRequestDestroyed(e.getServletRequest());
      this.request.set(null);
   }
   
   protected void responseInitialized(@Observes @Initialized final InternalServletResponseEvent e)
   {
      log.servletResponseInitialized(e.getServletResponse());
      this.response.set(e.getServletResponse());
   }
   
   protected void responseDestroyed(@Observes @Destroyed final InternalServletResponseEvent e)
   {
      log.servletResponseDestroyed(e.getServletResponse());
      this.response.set(null);
   }
   
   public ServletContext getServletContext()
   {
      return servletContext;
   }
   
   public ServletRequest getServletRequest()
   {
      return request.get();
   }
   
   public HttpServletRequest getHttpServletRequest()
   {
      return (HttpServletRequest) (request.get() instanceof HttpServletRequest ? request.get() : null);
   }
   
   public ServletResponse getServletResponse()
   {
      return response.get();
   }
   
   public HttpServletResponse getHttpServletResponse()
   {
      return (HttpServletResponse) (response.get() instanceof HttpServletResponse ? response.get() : null);
   }
   
   public HttpSession getHttpSession()
   {
      if (request.get() instanceof HttpServletRequest)
      {
         return ((HttpServletRequest) request.get()).getSession();
      }
      else
      {
         return null;
      }
   }
   
   static class InternalServletContextEvent
   {
      private ServletContext ctx;
      
      InternalServletContextEvent(ServletContext ctx)
      {
         this.ctx = ctx;
      }
      
      public ServletContext getServletContext()
      {
         return ctx;
      }
   }
   
   static class InternalServletRequestEvent
   {
      private ServletRequest request;
      
      InternalServletRequestEvent(ServletRequest request)
      {
         this.request = request;
      }
      
      public ServletRequest getServletRequest()
      {
         return request;
      }
   }

   static class InternalServletResponseEvent
   {
      private ServletResponse response;

      InternalServletResponseEvent(ServletResponse response)
      {
         this.response = response;
      }

      public ServletResponse getServletResponse()
      {
         return response;
      }
   }
   
   static class InternalHttpSessionEvent
   {
      private HttpSession session;

      InternalHttpSessionEvent(HttpSession session)
      {
         this.session = session;
      }

      public HttpSession getHttpSession()
      {
         return session;
      }
   }
}
