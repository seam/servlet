package org.jboss.seam.servlet.test.http;

import java.util.List;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.api.Deployment;
import org.jboss.seam.servlet.ServletExtension;
import org.jboss.seam.servlet.WebApplication;
import org.jboss.seam.servlet.beanManager.ServletContextAttributeProvider;
import org.jboss.seam.servlet.event.ServletEventBridgeFilter;
import org.jboss.seam.servlet.event.ServletEventBridgeListener;
import org.jboss.seam.servlet.http.ContextPath;
import org.jboss.seam.servlet.http.HttpServletRequestContext;
import org.jboss.seam.servlet.http.ImplicitHttpServletObjectsProducer;
import org.jboss.seam.servlet.test.event.ServletEventBridgeTestHelper;
import org.jboss.seam.servlet.test.event.ServletEventBridgeTestHelper.NoOpFilterChain;
import org.jboss.seam.servlet.test.util.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Gencur
 */
@RunWith(Arquillian.class)
public class ImplicitHttpServletObjectsProducerTest
{
   @Deployment
   public static Archive<?> createDeployment() {
      return Deployments
          .createMockableBeanWebArchive()
          .addClasses(ServletExtension.class, ServletContextAttributeProvider.class)
          .addPackage(WebApplication.class.getPackage())
          .addPackages(true, ServletEventBridgeListener.class.getPackage(),
              ImplicitHttpServletObjectsProducer.class.getPackage())
          .addClasses(ServletContextAttributeProvider.class, HttpServletRequestContext.class, 
                ServletEventBridgeTestHelper.class)
          .addServiceProvider(Extension.class, ServletExtension.class);
   }
   
   @Inject
   private Instance<HttpServletRequestContext> context;
   
   @Inject
   private Instance<HttpServletRequest> request;
   
   @Inject 
   @ContextPath
   private Instance<String> contextPathProvider;
   
   @Inject
   private Instance<List<Cookie>> cookies;
   
   @Inject
   private Instance<HttpSession> httpSession;
   
   @Inject
   private ServletEventBridgeListener listener;

   @Inject
   private ServletEventBridgeFilter filter;
   
   @Test
   public void should_inject_http_servlet_request_context() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      HttpServletRequest req = mock(HttpServletRequest.class);
      when(req.getContextPath()).thenReturn("servletRequestContextPath");
      when(req.getServletContext()).thenReturn(ctx);
      HttpServletResponse res = mock(HttpServletResponse.class);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      filter.doFilter(req, res, NoOpFilterChain.INSTANCE);

      Assert.assertEquals("servletRequestContextPath", context.get().getRequest().getContextPath());
   }
   
   @Test
   public void should_inject_http_servlet_request() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      HttpServletRequest req = mock(HttpServletRequest.class);
      when(req.getServletContext()).thenReturn(ctx);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      
      Assert.assertNotNull(request.get());
   }
   
   @Test
   public void should_inject_http_session() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      HttpServletRequest req = mock(HttpServletRequest.class);
      when(req.getServletContext()).thenReturn(ctx);
      HttpSession localHttpSession = mock(HttpSession.class);
      when(localHttpSession.getId()).thenReturn("mySessionId");
      when(req.getSession()).thenReturn(localHttpSession);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      
      Assert.assertEquals("mySessionId", httpSession.get().getId());
   }
   
   @Test
   public void should_inject_cookies() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      HttpServletRequest req = mock(HttpServletRequest.class);
      Cookie[] locCookies = new Cookie[] {new Cookie("place", "Brno"), new Cookie("event", "OpenHouse")};
      when(req.getCookies()).thenReturn(locCookies);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      
      Assert.assertEquals("place", cookies.get().get(0).getName());
      Assert.assertEquals("Brno", cookies.get().get(0).getValue());
      Assert.assertEquals("event", cookies.get().get(1).getName());
      Assert.assertEquals("OpenHouse", cookies.get().get(1).getValue());
   }
   
   @Test
   public void should_inject_context_path() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      HttpServletRequest req = mock(HttpServletRequest.class);
      when(req.getServletContext()).thenReturn(ctx);
      when(req.getContextPath()).thenReturn("extraContextPath");
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      
      Assert.assertEquals("extraContextPath", contextPathProvider.get());
   }
}
