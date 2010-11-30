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

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.seam.servlet.event.qualifier.literal.AddedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.AttributeLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.BoundLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.DestroyedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.DidActivateLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.InitializedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.RemovedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.ReplacedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.UnboundLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.ValueLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.WillPassivateLiteral;

/**
 * A servlet listener that propagates the events to the current CDI Bean Manager
 * event queue
 * 
 * @author Nicklas Karlsson
 */
public class Servlet2EventBridge extends AbstractServletEventBridge implements
      HttpSessionActivationListener, HttpSessionAttributeListener, HttpSessionBindingListener,
      HttpSessionListener, ServletContextListener, ServletContextAttributeListener,
      ServletRequestListener, ServletRequestAttributeListener
{

   /**
    * Session activated / passivated events
    */

   public void sessionDidActivate(final HttpSessionEvent e)
   {
      //fireEvent(e, DidActivateLiteral.INSTANCE);
      fireEvent(e.getSession(), DidActivateLiteral.INSTANCE);
   }

   public void sessionWillPassivate(final HttpSessionEvent e)
   {
      //fireEvent(e, WillPassivateLiteral.INSTANCE);
      fireEvent(e.getSession(), WillPassivateLiteral.INSTANCE);
   }

   /**
    * Session attribute events
    */

   public void attributeAdded(final HttpSessionBindingEvent e)
   {
      fireEvent(e, AddedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final HttpSessionBindingEvent e)
   {
      fireEvent(e, RemovedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final HttpSessionBindingEvent e)
   {
      fireEvent(e, ReplacedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void valueBound(final HttpSessionBindingEvent e)
   {
      fireEvent(e, BoundLiteral.INSTANCE, new ValueLiteral(e.getName()));
   }

   public void valueUnbound(final HttpSessionBindingEvent e)
   {
      fireEvent(e, UnboundLiteral.INSTANCE, new ValueLiteral(e.getName()));
   }

   /**
    * Session created / destroyed events
    */

   public void sessionCreated(final HttpSessionEvent e)
   {
      //fireEvent(e, InitializedLiteral.INSTANCE);
      fireEvent(e.getSession(), InitializedLiteral.INSTANCE);
   }

   public void sessionDestroyed(final HttpSessionEvent e)
   {
      //fireEvent(e, DestroyedLiteral.INSTANCE);
      fireEvent(e.getSession(), DestroyedLiteral.INSTANCE);
   }

   /**
    * Servlet context initialized / destroyed events
    */
   
   public void contextInitialized(final ServletContextEvent e)
   {
      //fireEvent(e, InitializedLiteral.INSTANCE);
      fireEvent(e.getServletContext(), InitializedLiteral.INSTANCE);
   }

   public void contextDestroyed(final ServletContextEvent e)
   {
      //fireEvent(e, DestroyedLiteral.INSTANCE);
      fireEvent(e.getServletContext(), DestroyedLiteral.INSTANCE);
   }

   /**
    * Servlet context attribute events
    */

   public void attributeAdded(final ServletContextAttributeEvent e)
   {
      fireEvent(e, AddedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final ServletContextAttributeEvent e)
   {
      fireEvent(e, RemovedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final ServletContextAttributeEvent e)
   {
      fireEvent(e, ReplacedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   /**
    * Request created / destroyed events
    */

   public void requestDestroyed(final ServletRequestEvent e)
   {
      //fireEvent(e, DestroyedLiteral.INSTANCE);
      fireEvent(e.getServletRequest(), DestroyedLiteral.INSTANCE);
   }

   public void requestInitialized(final ServletRequestEvent e)
   {
      //fireEvent(e, InitializedLiteral.INSTANCE);
      fireEvent(e.getServletRequest(), InitializedLiteral.INSTANCE);
   }

   /**
    * Servlet request attribute events
    */

   public void attributeAdded(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, AddedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, RemovedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, ReplacedLiteral.INSTANCE, new AttributeLiteral(e.getName()));
   }
}
