package org.jboss.seam.servlet.event;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.servlet.event.qualifier.Initialized;

@RequestScoped
public class HttpManager
{
   private HttpSession session;
   private HttpServletRequest request;
   private ServletContext context;

   public void refresh(@Observes @Initialized ServletRequestEvent e)
   {
      this.request = (HttpServletRequest) e.getServletRequest();
      session = request.getSession();
      context = session.getServletContext();
   }

   public HttpSession getSession()
   {
      return session;
   }

   public HttpServletRequest getRequest()
   {
      return request;
   }

   public ServletContext getContext()
   {
      return context;
   }
}
