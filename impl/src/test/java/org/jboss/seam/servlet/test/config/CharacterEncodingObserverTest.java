package org.jboss.seam.servlet.test.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.servlet.ServletExtension;
import org.jboss.seam.servlet.WebApplication;
import org.jboss.seam.servlet.config.CharacterEncodingConfig;
import org.jboss.seam.servlet.event.ServletEventBridgeFilter;
import org.jboss.seam.servlet.event.ServletEventBridgeListener;
import org.jboss.seam.servlet.http.RequestParam;
import org.jboss.seam.servlet.test.event.ServletEventBridgeTestHelper.NoOpFilterChain;
import org.jboss.seam.servlet.test.event.ServletEventBridgeTestHelper;
import org.jboss.seam.servlet.test.util.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class CharacterEncodingObserverTest
{
   private static final String NEW_ENCODING = "en_US";
   
   @Deployment
   public static Archive<?> createDeployment() {
       return Deployments.createMockableBeanWebArchive()
                         .addPackage(WebApplication.class.getPackage())
                         .addPackages(true, ServletEventBridgeListener.class.getPackage(),RequestParam.class.getPackage())
                         .addClasses(ServletEventBridgeTestHelper.class, CharacterEncodingConfig.class)
                         .addServiceProvider(Extension.class, ServletExtension.class);
   }
   
   @Inject
   CharacterEncodingConfig conf;
   
   @Inject
   ServletEventBridgeListener listener;

   @Inject
   ServletEventBridgeFilter filter;
   
   @Test
   public void should_override_character_encoding() throws Exception {
       ServletContext ctx = mock(ServletContext.class);
       HttpServletRequest req = mock(HttpServletRequest.class);
       HttpServletResponse res = mock(HttpServletResponse.class);
       when(req.getServletContext()).thenReturn(ctx);
       //set encoding observer's parameters
       conf.setEncoding(NEW_ENCODING);
       conf.setOverride(true);
       listener.requestInitialized(new ServletRequestEvent(ctx, req));
       filter.doFilter(req, res, NoOpFilterChain.INSTANCE);
       //verify those methods were called exactly once for request and response (in the observer)
       verify(req).setCharacterEncoding(NEW_ENCODING);
       verify(res).setCharacterEncoding(NEW_ENCODING);
   }
}
