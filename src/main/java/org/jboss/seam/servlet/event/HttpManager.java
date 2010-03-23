package org.jboss.seam.servlet.event;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.jboss.seam.servlet.event.qualifier.AttributeAdded;
import org.jboss.seam.servlet.event.qualifier.AttributeRemoved;
import org.jboss.seam.servlet.event.qualifier.AttributeReplaced;
import org.jboss.seam.servlet.event.qualifier.Created;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.Initialized;

/**
 * A manager for acquiring HTTP artifacts
 * 
 * @author Nicklas Karlsson
 * 
 */
@SessionScoped
public class HttpManager
{
   private HttpSession session;
   private HttpServletRequest request;
   private BeanManager beanManager;

   protected void requestInitialized(@Observes @Initialized ServletRequestEvent e)
   {
      request = (HttpServletRequest) e.getServletRequest();
   }

   protected void requestDestroyed(@Observes @Destroyed ServletRequestEvent e)
   {
      request = null;
   }

   protected void servletContextAttributeAdded(@Observes @AttributeAdded ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         beanManager = (BeanManager) e.getValue();
      }
   }

   protected void servletContextAttributeReplaced(@Observes @AttributeReplaced ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         beanManager = (BeanManager) e.getValue();
      }
   }

   protected void servletContextAttributeRemoved(@Observes @AttributeRemoved ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         beanManager = null;
      }
   }

   protected void sessionInitialized(@Observes @Created HttpSessionEvent e)
   {
      session = e.getSession();
   }

   protected void sessionDestroyed(@Observes @Destroyed HttpSessionEvent e)
   {
      session = null;
   }

   /**
    * Returns the current HTTP session. Throws an {@link IllegalStateException}
    * if the session is currently not set.
    * 
    * @return The current HTTP session
    */
   public HttpSession getSession()
   {
      if (session == null)
      {
         throw new IllegalStateException("The HTTP session is currently not set");
      }
      return session;
   }

   /**
    * Returns the current HTTP request. Throws an {@link IllegalStateException}
    * if the request is currently not set.
    * 
    * @return The current HTTP request
    */
   public HttpServletRequest getRequest()
   {
      if (request == null)
      {
         throw new IllegalStateException("The HTTP request is currently not set");
      }
      return request;
   }

   /**
    * Returns the current CDI Bean Manager of the WAR. Throws an
    * {@link IllegalStateException} if the manager is not set.
    * 
    * @return The current HTTP request
    */
   public BeanManager getBeanManager()
   {
      if (beanManager == null)
      {
         throw new IllegalStateException("The Bean Manager is currently not set");
      }
      return beanManager;
   }

}
