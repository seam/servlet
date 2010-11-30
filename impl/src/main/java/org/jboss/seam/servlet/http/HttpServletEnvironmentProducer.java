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
package org.jboss.seam.servlet.http;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.jboss.seam.servlet.log.ServletLog;
import org.jboss.weld.extensions.log.Category;

/**
 * A manager for acquiring HTTP artifacts
 * 
 * @author Nicklas Karlsson
 * @author <a href="mailto:dan.j.allen@gmail.com">Dan Allen</a>
 */
@ApplicationScoped
public class HttpServletEnvironmentProducer implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Inject @Category("seam-servlet")
   private ServletLog log;

   private final ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>()
   {
      @Override
      protected HttpSession initialValue()
      {
         return null;
      }
   };
   
   private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>()
   {
      @Override
      protected HttpServletRequest initialValue()
      {
         return null;
      }
   };

   protected void requestInitialized(@Observes @Initialized final HttpServletRequest req)
   {
      log.servletRequestInitialized(req);
      request.set(req);
      // QUESTION should we be forcing the session to be created here?
      session.set(req.getSession());
   }

   protected void requestDestroyed(@Observes @Destroyed final HttpServletRequest req)
   {
      log.servletRequestDestroyed(req);
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
   
   @Produces
   @RequestScoped
   protected List<Cookie> getCookies()
   {
      return Arrays.asList(request.get().getCookies());
   }
}
