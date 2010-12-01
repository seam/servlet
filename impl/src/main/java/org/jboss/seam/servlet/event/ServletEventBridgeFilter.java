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
