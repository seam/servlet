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
import org.jboss.seam.servlet.event.Servlet2EventBridge;
import org.jboss.seam.servlet.test.util.Deployments;
import org.jboss.seam.servlet.test.util.MavenArtifactResolver;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nicklas Karlsson
 * @author <a href="mailto:dan.j.allen@gmail.com">Dan Allen</a>
 */
@RunWith(Arquillian.class)
public class ServletEventBridgeTest
{
    @Deployment
    public static Archive<?> createDeployment()
    {
       return Deployments.createMockableBeanWebArchive()
          .addLibrary(MavenArtifactResolver.resolve("org.mockito:mockito-all:1.8.4"))
          .addPackages(true, Servlet2EventBridge.class.getPackage())
          .addClass(ServletEventBridgeTestHelper.class);
    }

    @Inject Servlet2EventBridge listener;
   
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
    public void should_observe_servlet_context_destroyed()
    {
       reset();
       ServletContext ctx = mock(ServletContext.class);
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
   
   /*
    * @Test public void testObserveAnySessionBindingEvent() { observer.reset();
    * HttpSessionBindingEvent e1 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e2 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e3 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e4 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e5 = new HttpSessionBindingEvent(null, "");
    * listener.attributeAdded(e1); listener.attributeRemoved(e2);
    * listener.attributeReplaced(e3); listener.valueBound(e4);
    * listener.valueUnbound(e5); observer.assertObservations("6", e1, e2, e3,
    * e4, e5); }
    * 
    * @Test public void testObserveSessionAttributeAdded() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.attributeAdded(e); observer.assertObservations("7", e); }
    * 
    * @Test public void testObserveSessionAttributeReplaced() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeReplaced(e);
    * observer.assertObservations("8", e); }
    * 
    * @Test public void testObserveSessionAttributeRemoved() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.attributeRemoved(e); observer.assertObservations("9", e); }
    * 
    * @Test public void testObserveSessionValueBound() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.valueBound(e); observer.assertObservations("10", e); }
    * 
    * @Test public void testObserveSessionValueUnbound() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.valueUnbound(e); observer.assertObservations("11", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeAdded() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeAdded(e);
    * observer.assertObservations("12", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeReplaced() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeReplaced(e);
    * observer.assertObservations("13", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeRemoved() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeRemoved(e);
    * observer.assertObservations("14", e); }
    * 
    * @Test public void testObserveSpecificSessionAttribute() {
    * observer.reset(); HttpSessionBindingEvent e1 = new
    * HttpSessionBindingEvent(null, ""); HttpSessionBindingEvent e2 = new
    * HttpSessionBindingEvent(null, ""); HttpSessionBindingEvent e3 = new
    * HttpSessionBindingEvent(null, ""); listener.attributeAdded(e1);
    * listener.attributeRemoved(e2); listener.attributeReplaced(e3);
    * observer.assertObservations("14a", e1, e2, e3); }
    * 
    * @Test public void testObserveSpecificSessionValueBound() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.valueBound(e);
    * observer.assertObservations("15", e); }
    * 
    * @Test public void testObserveSpecificSessionValueUnbound() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.valueUnbound(e);
    * observer.assertObservations("16", e); }
    * 
    * @Test public void testObserveSpecificSessionValue() { observer.reset();
    * HttpSessionBindingEvent e1 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e2 = new HttpSessionBindingEvent(null, "");
    * listener.valueBound(e1); listener.valueUnbound(e2);
    * observer.assertObservations("16a", e1, e2); }
    *
    * @Test public void testObserveServletContextAttribute() { observer.reset();
    * ServletContextAttributeEvent e1 = new ServletContextAttributeEvent(null,
    * "", null); ServletContextAttributeEvent e2 = new
    * ServletContextAttributeEvent(null, "", null); ServletContextAttributeEvent
    * e3 = new ServletContextAttributeEvent(null, "", null);
    * listener.attributeAdded(e1); listener.attributeRemoved(e2);
    * listener.attributeReplaced(e3); observer.assertObservations("20", e1, e2,
    * e3); }
    * 
    * @Test public void testObserveAnyServletContextAttributeAdded() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null); listener.attributeAdded(e);
    * observer.assertObservations("21", e); }
    * 
    * @Test public void testObserveAnyServletContextAttributeReplaced() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("22", e); }
    * 
    * @Test public void testObserveAnyServletContextAttributeRemoved() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("23", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeAdded() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null); listener.attributeAdded(e);
    * observer.assertObservations("24", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeReplaced() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("25", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeRemoved() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("26", e); }

    * @Test public void testObserveAnyServletRequestAttributeAdded() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeAdded(e); observer.assertObservations("30", e); }
    * 
    * @Test public void testObserveAnyServletRequestAttributeReplaced() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("31", e); }
    * 
    * @Test public void testObserveAnyServletRequestAttributeRemoved() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("32", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeAdded() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeAdded(e); observer.assertObservations("33", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeReplaced() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("34", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeRemoved() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("35", e); }
    * 
    * @Test public void testObserveAsynchrnousEventCompleted() throws
    * IOException { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onComplete(e); observer.assertObservations("36", e); }
    * 
    * @Test public void testObserveAsynchrnousEventError() throws IOException {
    * observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onError(e); observer.assertObservations("37", e); }
    * 
    * @Test public void testObserveAsynchrnousEventStarted() throws IOException
    * { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onStartAsync(e); observer.assertObservations("38", e); }
    * 
    * @Test public void testObserveAsynchrnousEventTimedOut() throws IOException
    * { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onTimeout(e); observer.assertObservations("39", e); }
    * 
    * @Test public void testObserveAsynchrnousEventT() throws IOException {
    * observer.reset(); AsyncEvent e1 = new AsyncEvent(null); AsyncEvent e2 =
    * new AsyncEvent(null); AsyncEvent e3 = new AsyncEvent(null); AsyncEvent e4
    * = new AsyncEvent(null); listener.onComplete(e1); listener.onError(e2);
    * listener.onStartAsync(e3); listener.onTimeout(e4);
    * observer.assertObservations("40", e1, e2, e3, e4); }
    */
}
