package org.jboss.seam.servlet.filter;

import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.seam.exception.control.ExceptionToCatchEvent;
import org.jboss.seam.servlet.http.literal.HttpRequestLiteral;

/**
 * A bridge that forwards unhandled exceptions to the Seam exception handling facility (Seam Catch).
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class CatchExceptionFilter implements Filter
{
   @Inject
   private Event<ExceptionToCatchEvent> bridgeEvent;
   
   public void init(FilterConfig config) throws ServletException
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      try
      {
         chain.doFilter(request, response);
      }
      catch (Exception e)
      {
         ExceptionToCatchEvent catchEvent = new ExceptionToCatchEvent(e, HttpRequestLiteral.INSTANCE);
         bridgeEvent.fire(catchEvent);
         // QUESTION shouldn't catch handle rethrowing?
         if (!catchEvent.isHandled())
         {
            if (e instanceof ServletException)
            {
               throw (ServletException) e;
            }
            else if (e instanceof IOException)
            {
               throw (IOException) e;
            }
         }
      }
   }

   public void destroy()
   {
   }
}
