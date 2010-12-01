package org.jboss.seam.servlet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.seam.servlet.event.ImplicitServletObjectsHolder;

/**
 * Produces an application-scoped {@link ServletContext} bean.
 * A references is obtained from the {@link ImplicitServletObjectsHolder}.
 *
 * @author Dan Allen
 */
public class ImplicitServletObjectsProducer
{
   @Inject
   private ImplicitServletObjectsHolder holder;
   
   @Produces
   @ApplicationScoped
   public ServletContext getServletContext()
   {
      return holder.getServletContext();
   }
   
   @Produces
   @RequestScoped
   public ServletRequest getServletRequest()
   {
      return holder.getServletRequest();
   }
   
   @Produces
   @RequestScoped
   public ServletResponse getServletResponse()
   {
      return holder.getServletResponse();
   }
}
