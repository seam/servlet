package org.jboss.seam.servlet.event;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
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
import org.slf4j.Logger;

/**
 * A manager for acquiring HTTP artifacts
 * 
 * @author Nicklas Karlsson
 * 
 */
@RequestScoped
public class HttpManager
{
   private static final long serialVersionUID = 5191073522575178427L;
   
   private HttpSession session;
   private HttpServletRequest request;
   private BeanManager beanManager;

   @Inject
   private Logger log;
   
   protected void requestInitialized(@Observes @Initialized ServletRequestEvent e)
   {
      log.trace("Servlet request initialized with event #0", e);
      request = (HttpServletRequest) e.getServletRequest();
   }

   protected void requestDestroyed(@Observes @Destroyed ServletRequestEvent e)
   {
      log.trace("Servlet request destroyed with event #0", e);
      request = null;
   }

   protected void servletContextAttributeAdded(@Observes @AttributeAdded ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         log.trace("Bean manager set in servlet context with event #0", e);
         beanManager = (BeanManager) e.getValue();
      }
   }

   protected void servletContextAttributeReplaced(@Observes @AttributeReplaced ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         log.trace("Bean manager replaced in servlet context with event #0", e);
         beanManager = (BeanManager) e.getValue();
      }
   }

   protected void servletContextAttributeRemoved(@Observes @AttributeRemoved ServletContextAttributeEvent e)
   {
      if (BeanManager.class.getName().equals(e.getName()))
      {
         log.trace("Bean manager removed from servlet context with event #0", e);
         beanManager = null;
      }
   }

   protected void sessionInitialized(@Observes @Created HttpSessionEvent e)
   {
      log.trace("HTTP session initalized with event #0", e);
      session = e.getSession();
   }

   protected void sessionDestroyed(@Observes @Destroyed HttpSessionEvent e)
   {
      log.trace("HTTP session destroyed with event #0", e);
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

   @Produces
   @HttpParam("")
   String getParamValue(InjectionPoint ip)
   {
      return getRequest().getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
   }

}
