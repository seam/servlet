package org.jboss.seam.servlet.event;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.seam.servlet.event.qualifier.AttributeAdded;
import org.jboss.seam.servlet.event.qualifier.AttributeRemoved;
import org.jboss.seam.servlet.event.qualifier.AttributeReplaced;
import org.jboss.seam.servlet.event.qualifier.Complete;
import org.jboss.seam.servlet.event.qualifier.Created;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.DidActivate;
import org.jboss.seam.servlet.event.qualifier.Error;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.jboss.seam.servlet.event.qualifier.StartAsync;
import org.jboss.seam.servlet.event.qualifier.Timeout;
import org.jboss.seam.servlet.event.qualifier.ValueBound;
import org.jboss.seam.servlet.event.qualifier.ValueUnbound;
import org.jboss.seam.servlet.event.qualifier.WillPassivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A self-registering web-listener that propagates the events to the current CDI
 * Bean Manager event queue
 * 
 * @author Nicklas Karlsson
 * 
 */
@WebListener
public class ServletEventListener implements HttpSessionActivationListener, HttpSessionAttributeListener, HttpSessionBindingListener, HttpSessionListener, ServletContextListener, ServletContextAttributeListener, ServletRequestListener, ServletRequestAttributeListener, AsyncListener
{
   private BeanManager beanManager;

   private Logger log = LoggerFactory.getLogger(ServletEventListener.class);

   // FIXME: hack to work around invalid binding in JBoss AS 6 M2
   private static final List<String> beanManagerLocations = new ArrayList<String>()
   {
      private static final long serialVersionUID = 1L;
      {
         add("java:comp/BeanManager");
         add("java:app/BeanManager");
      }
   };

   public ServletEventListener()
   {
      beanManager = lookupBeanManager();
   }

   private BeanManager lookupBeanManager()
   {
      for (String location : beanManagerLocations)
      {
         try
         {
            log.trace("Looking for Bean Manager at JNDI location #0", location);
            return (BeanManager) new InitialContext().lookup(location);
         }
         catch (NamingException e)
         {
            // No panic, keep trying
            log.debug("Bean Manager not found at JNDI location #0", location);
         }
      }
      // OK, panic
      throw new IllegalArgumentException("Could not find BeanManager in " + beanManagerLocations);
   }

   private void fireEvent(Object payload, Annotation... qualifiers)
   {
      log.trace("Firing event #0 with qualifiers #1", payload, qualifiers);
      beanManager.fireEvent(payload, qualifiers);
   }

   /**
    * Session activated / passivated events
    */

   public void sessionDidActivate(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<DidActivate>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void sessionWillPassivate(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<WillPassivate>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Session attribute events
    */

   public void attributeAdded(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeRemoved(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeReplaced(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void valueBound(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<ValueBound>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void valueUnbound(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<ValueUnbound>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Session created / destroyed events
    */

   public void sessionCreated(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Created>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void sessionDestroyed(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Servlet context initialized / destroyed events
    */

   public void contextDestroyed(ServletContextEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void contextInitialized(ServletContextEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Initialized>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Servlet context attribute events
    */

   public void attributeAdded(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeRemoved(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeReplaced(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Request created / destroyed events
    */

   public void requestDestroyed(ServletRequestEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void requestInitialized(ServletRequestEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Initialized>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Servlet request attribute events
    */

   public void attributeAdded(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeRemoved(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void attributeReplaced(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   /**
    * Asynchronous events
    */

   public void onComplete(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Complete>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void onError(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Error>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void onStartAsync(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<StartAsync>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

   public void onTimeout(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Timeout>()
      {
         private static final long serialVersionUID = 1L;
      });
   }

}
