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
package org.jboss.seam.servlet.test.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.servlet.WebApplication;
import org.jboss.seam.servlet.beanManager.ServletContextAttributeProvider;
import org.jboss.seam.servlet.event.ServletEventBridgeListener;
import org.jboss.seam.servlet.test.util.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nicklas Karlsson
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@RunWith(Arquillian.class)
public class ServletEventBridgeTest
{
    @Deployment
    public static Archive<?> createDeployment()
    {
       return Deployments.createMockableBeanWebArchive()
          .addPackages(true, ServletEventBridgeListener.class.getPackage())
          .addClasses(ServletEventBridgeTestHelper.class, ServletContextAttributeProvider.class, WebApplication.class);
    }

    @Inject ServletEventBridgeListener listener;
   
    @Inject ServletEventBridgeTestHelper observer;

    //@Before
    public void reset()
    {
       observer.reset();
    }
    
    @Test
    public void should_observe_servlet_context()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       listener.contextInitialized(new ServletContextEvent(ctx));
       listener.contextDestroyed(new ServletContextEvent(ctx));
       observer.assertObservations("ServletContext", ctx, ctx);
    }
    
    @Test
    public void should_observe_servlet_context_initialized()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       listener.contextInitialized(new ServletContextEvent(ctx));
       observer.assertObservations("@Initialized ServletContext", ctx);
    }
    
    @Test
    public void should_observe_servlet_context_destroyed() throws Exception
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       // webApplication field is normally set by context initialized event
       Field webAppField = ServletEventBridgeListener.class.getDeclaredField("webApplication");
       webAppField.setAccessible(true);
       webAppField.set(listener, new WebApplication(ctx));
       listener.contextDestroyed(new ServletContextEvent(ctx));
       observer.assertObservations("@Destroyed ServletContext", ctx);
    }
  
    @Test
    public void should_observe_session()
    {
       reset();
       HttpSession session = mock(HttpSession.class);
       listener.sessionCreated(new HttpSessionEvent(session));
       listener.sessionWillPassivate(new HttpSessionEvent(session));
       listener.sessionDidActivate(new HttpSessionEvent(session));
       listener.sessionDestroyed(new HttpSessionEvent(session));
       observer.assertObservations("HttpSession", session, session, session, session);
    }
    
    @Test
    public void should_observe_session_created()
    {
       reset();
       HttpSession session = mock(HttpSession.class);
       listener.sessionCreated(new HttpSessionEvent(session));
       observer.assertObservations("@Initialized HttpSession", session);
    }
    
    @Test
    public void should_observe_session_destroyed()
    {
       reset();
       HttpSession session = mock(HttpSession.class);
       listener.sessionDestroyed(new HttpSessionEvent(session));
       observer.assertObservations("@Destroyed HttpSession", session);
    }
    
    @Test
    public void should_observe_session_will_passivate()
    {
       reset();
       HttpSession session = mock(HttpSession.class);
       listener.sessionWillPassivate(new HttpSessionEvent(session));
       observer.assertObservations("@WillPassivate HttpSession", session);
    }
    
    @Test
    public void should_observe_session_did_activate()
    {
       reset();
       HttpSession session = mock(HttpSession.class);
       listener.sessionDidActivate(new HttpSessionEvent(session));
       observer.assertObservations("@DidActivate HttpSession", session);
    }
 
    @Test
    public void should_observe_request()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       ServletRequest req = mock(ServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestInitialized(new ServletRequestEvent(ctx, req));
       listener.requestDestroyed(new ServletRequestEvent(ctx, req));
       observer.assertObservations("ServletRequest", req, req);
       observer.assertObservations("HttpServletRequest");
    }
    
    @Test
    public void should_observe_request_initialized()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       ServletRequest req = mock(ServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestInitialized(new ServletRequestEvent(ctx, req));
       observer.assertObservations("@Initialized ServletRequest", req);
       observer.assertObservations("@Initialized HttpServletRequest");
    }
    
    @Test
    public void should_observe_request_destroyed()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       ServletRequest req = mock(ServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestDestroyed(new ServletRequestEvent(ctx, req));
       observer.assertObservations("@Destroyed ServletRequest", req);
       observer.assertObservations("@Destroyed HttpServletRequest");
    }
    
    @Test
    public void should_observe_http_request()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       HttpServletRequest req = mock(HttpServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestInitialized(new ServletRequestEvent(ctx, req));
       listener.requestDestroyed(new ServletRequestEvent(ctx, req));
       observer.assertObservations("ServletRequest", req, req);
       observer.assertObservations("HttpServletRequest", req, req);
    }
    
    @Test
    public void should_observe_http_request_initialized()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       HttpServletRequest req = mock(HttpServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestInitialized(new ServletRequestEvent(ctx, req));
       observer.assertObservations("@Initialized ServletRequest", req);
       observer.assertObservations("@Initialized HttpServletRequest", req);
    }
    
    @Test
    public void should_observe_http_request_destroyed()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
       HttpServletRequest req = mock(HttpServletRequest.class);
       when(req.getServletContext()).thenReturn(ctx);
       listener.requestDestroyed(new ServletRequestEvent(ctx, req));
       observer.assertObservations("@Destroyed ServletRequest", req);
       observer.assertObservations("@Destroyed HttpServletRequest", req);
    }
}
