package org.jboss.seam.servlet.event;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.seam.servlet.event.ImplicitServletObjectsHolder.InternalServletResponseEvent;
import org.jboss.seam.servlet.event.literal.DestroyedLiteral;
import org.jboss.seam.servlet.event.literal.InitializedLiteral;

/**
 * Propagates the {@link ServletResponse} lifecycle events to the CDI event
 * bus, complementing the ServletEventBridgeListener, which handles the other
 * lifecycle events.
 * 
 * <p>This filter is auto-registered in Servlet 3.0 environments. If CDI
 * injection is available into filters, the BeanManager will be accessible to
 * this instance as an injected resource. Otherwise, the BeanManager will be
 * looked up using the BeanManager provider service.</p>
 *
 * <p>The internal events are fired to ensure that the setup and tear down routines
 * happen around the main events. The event strategy is used to jump from
 * a Servlet component which may not be managed by CDI to an observe we know
 * to be a managed bean.</p>
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class ServletEventBridgeFilter extends AbstractServletEventBridge implements Filter
{
   public void init(FilterConfig config) throws ServletException
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      fireEvent(new InternalServletResponseEvent(response), InitializedLiteral.INSTANCE);
      fireEvent(response, InitializedLiteral.INSTANCE);
      
      try
      {
         chain.doFilter(request, response);
      }
      finally
      {
         fireEvent(response, DestroyedLiteral.INSTANCE);
         fireEvent(new InternalServletResponseEvent(response), DestroyedLiteral.INSTANCE);
      }
   }

   public void destroy()
   {
   }
}
