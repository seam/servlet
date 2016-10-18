package org.jboss.seam.servlet.test;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.api.Deployment;
import org.jboss.seam.servlet.ImplicitServletObjectsProducer;
import org.jboss.seam.servlet.ServerInfo;
import org.jboss.seam.servlet.ServletExtension;
import org.jboss.seam.servlet.ServletRequestContext;
import org.jboss.seam.servlet.WebApplication;
import org.jboss.seam.servlet.beanManager.ServletContextAttributeProvider;
import org.jboss.seam.servlet.event.ImplicitServletObjectsHolder;
import org.jboss.seam.servlet.event.ServletEventBridgeFilter;
import org.jboss.seam.servlet.event.ServletEventBridgeListener;
import org.jboss.seam.servlet.http.RequestParam;
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
public class ImplicitServletObjectsProducerTest
{
   @Deployment
   public static Archive<?> createDeployment() {
      return Deployments
          .createMockableBeanWebArchive()
          .addClasses(ServletExtension.class)
          .addPackage(WebApplication.class.getPackage())
          .addPackages(true, ServletEventBridgeListener.class.getPackage(),
              RequestParam.class.getPackage())
          .addClasses(ServletContextAttributeProvider.class, ServletRequestContext.class, 
              ServletEventBridgeTestHelper.class, ImplicitServletObjectsProducer.class,
              ImplicitServletObjectsHolder.class)
          .addServiceProvider(Extension.class, ServletExtension.class);
   }
   
   @Inject
   private Instance<ServletContext> servletCtx;
   
   @Inject
   private Instance<ServletRequestContext> requestCtx;
   
   @Inject
   private Instance<ServletRequest> request;
   
   @Inject
   @ServerInfo
   private Instance<String> serverInfo;
   
   @Inject
   private ServletEventBridgeListener listener;

   @Inject
   private ServletEventBridgeFilter filter;
   
   @Test
   public void should_inject_servlet_context() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      when(ctx.getServletContextName()).thenReturn("mock");
      listener.contextInitialized(new ServletContextEvent(ctx));

      Assert.assertEquals("mock", servletCtx.get().getServletContextName());
   }
   
   @Test
   public void should_inject_servlet_request_context() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      ServletRequest req = mock(ServletRequest.class);
      when(req.getContentType()).thenReturn("text/html");
      when(req.getServletContext()).thenReturn(ctx);
      ServletResponse res = mock(ServletResponse.class);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      filter.doFilter(req, res, NoOpFilterChain.INSTANCE);

      Assert.assertEquals("text/html", requestCtx.get().getRequest().getContentType());
   }
   
   @Test
   public void should_inject_server_info() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      when(ctx.getServerInfo()).thenReturn("JBossAS/6.0.0");
      listener.contextInitialized(new ServletContextEvent(ctx));
      
      Assert.assertEquals("JBossAS/6.0.0", serverInfo.get());
   }
   
   @Test
   public void should_inject_servlet_request() throws Exception {
      ServletContext ctx = mock(ServletContext.class);
      ServletRequest req = mock(ServletRequest.class);
      when(req.getServletContext()).thenReturn(ctx);
      listener.requestInitialized(new ServletRequestEvent(ctx, req));
      
      Assert.assertNotNull(request.get());
   }
}
