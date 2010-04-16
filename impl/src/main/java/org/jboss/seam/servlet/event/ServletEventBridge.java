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
package org.jboss.seam.servlet.event;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
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

import org.jboss.seam.servlet.event.qualifier.Added;
import org.jboss.seam.servlet.event.qualifier.Attribute;
import org.jboss.seam.servlet.event.qualifier.Bound;
import org.jboss.seam.servlet.event.qualifier.Completed;
import org.jboss.seam.servlet.event.qualifier.Created;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.DidActivate;
import org.jboss.seam.servlet.event.qualifier.Error;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.jboss.seam.servlet.event.qualifier.Removed;
import org.jboss.seam.servlet.event.qualifier.Replaced;
import org.jboss.seam.servlet.event.qualifier.StartAsync;
import org.jboss.seam.servlet.event.qualifier.Timeout;
import org.jboss.seam.servlet.event.qualifier.Unbound;
import org.jboss.seam.servlet.event.qualifier.Value;
import org.jboss.seam.servlet.event.qualifier.WillPassivate;
import org.slf4j.Logger;

/**
 * A servlet listener that propagates the events to the current CDI Bean Manager
 * event queue
 * 
 * @author Nicklas Karlsson
 * 
 */
public class ServletEventBridge implements HttpSessionActivationListener, HttpSessionAttributeListener, HttpSessionBindingListener, HttpSessionListener, ServletContextListener, ServletContextAttributeListener, ServletRequestListener, ServletRequestAttributeListener, AsyncListener
{
   @Inject
   private BeanManager beanManager;

   @Inject
   private Logger log;

   public ServletEventBridge()
   {
   }

   /**
    * Session activated / passivated events
    */

   public void sessionDidActivate(final HttpSessionEvent e)
   {
      fireEvent(e, DIDACTIVATE);
   }

   public void sessionWillPassivate(final HttpSessionEvent e)
   {
      fireEvent(e, WILLPASSIVATE);
   }

   /**
    * Session attribute events
    */

   public void attributeAdded(final HttpSessionBindingEvent e)
   {
      fireEvent(e, ADDED, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final HttpSessionBindingEvent e)
   {
      fireEvent(e, REMOVED, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final HttpSessionBindingEvent e)
   {
      fireEvent(e, REPLACED, new AttributeLiteral(e.getName()));
   }

   public void valueBound(final HttpSessionBindingEvent e)
   {
      fireEvent(e, BOUND, new ValueLiteral(e.getName()));
   }

   public void valueUnbound(final HttpSessionBindingEvent e)
   {
      fireEvent(e, UNBOUND, new ValueLiteral(e.getName()));
   }

   /**
    * Session created / destroyed events
    */

   public void sessionCreated(final HttpSessionEvent e)
   {
      fireEvent(e, CREATED);
   }

   public void sessionDestroyed(final HttpSessionEvent e)
   {
      fireEvent(e, DESTROYED);
   }

   /**
    * Servlet context initialized / destroyed events
    */

   public void contextDestroyed(final ServletContextEvent e)
   {
      fireEvent(e, DESTROYED);
   }

   public void contextInitialized(final ServletContextEvent e)
   {
      fireEvent(e, INITIALIZED);
   }

   /**
    * Servlet context attribute events
    */

   public void attributeAdded(final ServletContextAttributeEvent e)
   {
      fireEvent(e, ADDED, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final ServletContextAttributeEvent e)
   {
      fireEvent(e, REMOVED, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final ServletContextAttributeEvent e)
   {
      fireEvent(e, REPLACED, new AttributeLiteral(e.getName()));
   }

   /**
    * Request created / destroyed events
    */

   public void requestDestroyed(final ServletRequestEvent e)
   {
      fireEvent(e, DESTROYED);
   }

   public void requestInitialized(final ServletRequestEvent e)
   {
      fireEvent(e, INITIALIZED);
   }

   /**
    * Servlet request attribute events
    */

   public void attributeAdded(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, ADDED, new AttributeLiteral(e.getName()));
   }

   public void attributeRemoved(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, REMOVED, new AttributeLiteral(e.getName()));
   }

   public void attributeReplaced(final ServletRequestAttributeEvent e)
   {
      fireEvent(e, REPLACED, new AttributeLiteral(e.getName()));
   }

   /**
    * Asynchronous events
    */

   public void onComplete(final AsyncEvent e) throws IOException
   {
      fireEvent(e, COMPLETED);
   }

   public void onError(final AsyncEvent e) throws IOException
   {
      fireEvent(e, ERROR);
   }

   public void onStartAsync(final AsyncEvent e) throws IOException
   {
      fireEvent(e, STARTASYNCH);
   }

   public void onTimeout(final AsyncEvent e) throws IOException
   {
      fireEvent(e, TIMEOUT);
   }

   private void fireEvent(final Object payload, final Annotation... qualifiers)
   {
      log.trace("Firing event #0 with qualifiers #1", payload, qualifiers);
      beanManager.fireEvent(payload, qualifiers);
   }

   /*
    * Annotation Literal Constants
    */
   private static final AnnotationLiteral<WillPassivate> WILLPASSIVATE = new AnnotationLiteral<WillPassivate>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<DidActivate> DIDACTIVATE = new AnnotationLiteral<DidActivate>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Unbound> UNBOUND = new AnnotationLiteral<Unbound>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Bound> BOUND = new AnnotationLiteral<Bound>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Created> CREATED = new AnnotationLiteral<Created>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Destroyed> DESTROYED = new AnnotationLiteral<Destroyed>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Initialized> INITIALIZED = new AnnotationLiteral<Initialized>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Added> ADDED = new AnnotationLiteral<Added>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Removed> REMOVED = new AnnotationLiteral<Removed>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Replaced> REPLACED = new AnnotationLiteral<Replaced>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Completed> COMPLETED = new AnnotationLiteral<Completed>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Error> ERROR = new AnnotationLiteral<Error>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<StartAsync> STARTASYNCH = new AnnotationLiteral<StartAsync>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Timeout> TIMEOUT = new AnnotationLiteral<Timeout>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private class AttributeLiteral extends AnnotationLiteral<Attribute> implements Attribute
   {
      private final String value;

      public String value()
      {
         return value;
      }

      public AttributeLiteral(String value)
      {
         this.value = value;
      }
   }

   private class ValueLiteral extends AnnotationLiteral<Value> implements Value
   {
      private final String value;

      public String value()
      {
         return value;
      }

      public ValueLiteral(String value)
      {
         this.value = value;
      }
   }
}
