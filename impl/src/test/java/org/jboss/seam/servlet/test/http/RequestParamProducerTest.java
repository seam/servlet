/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.servlet.test.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.servlet.NarrowingBeanBuilder;
import org.jboss.seam.servlet.ServletExtension;
import org.jboss.seam.servlet.http.CookieParam;
import org.jboss.seam.servlet.http.DefaultValue;
import org.jboss.seam.servlet.http.HeaderParam;
import org.jboss.seam.servlet.http.HttpServletEnvironmentProducer;
import org.jboss.seam.servlet.http.RequestParam;
import org.jboss.seam.servlet.http.TypedParamValue;
import org.jboss.seam.servlet.test.util.Deployments;
import org.jboss.seam.servlet.util.Primitives;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:dan.j.allen@gmail.com">Dan Allen</a>
 */
@RunWith(Arquillian.class)
// TODO split up into individual tests for each param type
public class RequestParamProducerTest
{
   private static final String IMPLICIT_PARAM = "implicit";
   private static final String EXPLICIT_PARAM = "explicit";
   private static final String MISSING_PARAM = "missing";
   private static final String IMPLICIT_VALUE = IMPLICIT_PARAM + "Value";
   private static final String EXPLICIT_VALUE = EXPLICIT_PARAM + "Value";
   private static final String DEFAULT_VALUE = "defaultValue";
   
   @Deployment
   public static Archive<?> createDeployment()
   {
      return Deployments.createMockableBeanWebArchive()
         .addClasses(ServletExtension.class, Primitives.class, NarrowingBeanBuilder.class)
         .addPackages(false, Deployments.exclude(HttpServletEnvironmentProducer.class), TypedParamValue.class.getPackage())
         .addServiceProvider(Extension.class, ServletExtension.class);
   }
   
   @Inject @RequestParam(EXPLICIT_PARAM) Instance<String> explicit;
   
   @Inject @RequestParam Instance<String> implicit;
   
   @Inject @RequestParam(MISSING_PARAM) @DefaultValue(DEFAULT_VALUE) Instance<String> missing;
   
   @Inject @RequestParam(MISSING_PARAM) Instance<String> missingNoDefault;
   
   @Inject @RequestParam("pageSize") Instance<Integer> pageSize;
   
   @Inject @HeaderParam("Cache-Control") Instance<String> cacheControl;
   
   @Inject @CookieParam Instance<String> chocolate;
   
   @Test
   public void should_inject_value_for_explicit_http_param()
   {
      Assert.assertEquals(EXPLICIT_VALUE, explicit.get());
   }
   
   @Test
   public void should_inject_value_for_implicit_http_param()
   {
      Assert.assertEquals(IMPLICIT_VALUE, implicit.get());
   }
   
   @Test
   public void should_inject_default_value_for_missing_http_param()
   {
      Assert.assertEquals(DEFAULT_VALUE, missing.get());
   }
   
   @Test
   public void should_inject_null_value_for_missing_http_param()
   {
      Assert.assertNull(missingNoDefault.get());
   }
   
   @Test
   public void should_inject_value_for_typed_http_param(@RequestParam("page") int page)
   {
      Assert.assertEquals((Integer) 25, pageSize.get());
      Assert.assertEquals((Integer) 1, (Integer) page);
   }
   
   @Test
   public void should_inject_value_for_header_param()
   {
      Assert.assertEquals("no-cache", cacheControl.get());
   }
   
   @Test
   public void should_inject_value_for_cookie_param()
   {
      Assert.assertEquals("chip", chocolate.get());
   }
   
   @Produces
   public HttpServletRequest getHttpServletRequest()
   {
      HttpServletRequest req = mock(HttpServletRequest.class);
      
      Map<String, String[]> parameters = new HashMap<String, String[]>();
      parameters.put(IMPLICIT_PARAM, new String[] { IMPLICIT_VALUE });
      parameters.put(EXPLICIT_PARAM, new String[] { EXPLICIT_VALUE });
      parameters.put("page", new String[] { "1" });
      parameters.put("pageSize", new String[] { "25" });
      when(req.getParameterMap()).thenReturn(parameters);
      when(req.getParameter(IMPLICIT_PARAM)).thenReturn(IMPLICIT_VALUE);
      when(req.getParameter(EXPLICIT_PARAM)).thenReturn(EXPLICIT_VALUE);
      when(req.getParameter("page")).thenReturn("1");
      when(req.getParameter("pageSize")).thenReturn("25");
      
      Vector<String> headerNames = new Vector<String>();
      headerNames.add("Cache-Control");
      when(req.getHeaderNames()).thenReturn(headerNames.elements());
      when(req.getHeader("Cache-Control")).thenReturn("no-cache");
      
      Cookie[] cookies = new Cookie[] { new Cookie("chocolate", "chip") };
      when(req.getCookies()).thenReturn(cookies);
      
      return req;
   }
}
