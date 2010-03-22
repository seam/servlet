package org.jboss.seam.servlet.event;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
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

@SuppressWarnings("serial")
@WebListener
public class ServletListener implements HttpSessionActivationListener, HttpSessionAttributeListener, HttpSessionBindingListener, HttpSessionListener, ServletContextListener, ServletContextAttributeListener, ServletRequestListener, ServletRequestAttributeListener, AsyncListener
{
   @Inject
   private BeanManager beanManager;

   // FIXME: hack to work around invalid binding in JBoss AS 6 M2
   private static final List<String> beanManagerLocations = new ArrayList<String>()
   {
      {
         add("java:comp/BeanManager");
         add("java:app/BeanManager");
      }
   };

   public ServletListener()
   {
      if (beanManager == null)
      {
         beanManager = lookupBeanManager();
      }
   }

   private BeanManager lookupBeanManager()
   {
      for (String location : beanManagerLocations)
      {
         try
         {
            return (BeanManager) new InitialContext().lookup(location);
         }
         catch (NamingException e)
         {
            // No panic, keep trying
         }
      }
      // OK, panic
      throw new IllegalArgumentException("Could not find BeanManager in " + beanManagerLocations);
   }

   private void fireEvent(Object payload, Annotation... qualifiers)
   {
      System.out.println("Fired event " + payload + " with " + qualifiers);
      beanManager.fireEvent(payload, qualifiers);
   }

   public void sessionDidActivate(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<DidActivate>()
      {
      });
   }

   public void sessionWillPassivate(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<WillPassivate>()
      {
      });
   }

   public void attributeAdded(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
      });
   }

   public void attributeRemoved(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
      });
   }

   public void attributeReplaced(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
      });
   }

   public void valueBound(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<ValueBound>()
      {
      });
   }

   public void valueUnbound(HttpSessionBindingEvent e)
   {
      fireEvent(e, new AnnotationLiteral<ValueUnbound>()
      {
      });
   }

   public void sessionCreated(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Created>()
      {
      });
   }

   public void sessionDestroyed(HttpSessionEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
      });
   }

   public void contextDestroyed(ServletContextEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
      });
   }

   public void contextInitialized(ServletContextEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Initialized>()
      {
      });
   }

   public void attributeAdded(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
      });
   }

   public void attributeRemoved(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
      });
   }

   public void attributeReplaced(ServletContextAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
      });
   }

   public void requestDestroyed(ServletRequestEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Destroyed>()
      {
      });
   }

   public void requestInitialized(ServletRequestEvent e)
   {
      fireEvent(e, new AnnotationLiteral<Initialized>()
      {
      });
   }

   public void attributeAdded(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeAdded>()
      {
      });
   }

   public void attributeRemoved(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeRemoved>()
      {
      });
   }

   public void attributeReplaced(ServletRequestAttributeEvent e)
   {
      fireEvent(e, new AnnotationLiteral<AttributeReplaced>()
      {
      });
   }

   public void onComplete(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Complete>()
      {
      });
   }

   public void onError(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Error>()
      {
      });
   }

   public void onStartAsync(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<StartAsync>()
      {
      });
   }

   public void onTimeout(AsyncEvent e) throws IOException
   {
      fireEvent(e, new AnnotationLiteral<Timeout>()
      {
      });
   }

}
