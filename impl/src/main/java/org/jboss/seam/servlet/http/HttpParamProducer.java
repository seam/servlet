/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.servlet.http;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * A producer for {@link HttpParam}
 * 
 * @author Nicklas Karlsson
 * 
 */
public class HttpParamProducer
{
   @Inject
   private HttpServletRequest request;

   @Produces
   @HttpParam("")
   protected String getParamValue(InjectionPoint ip)
   {
      String parameterName = getParameterName(ip);
      return getParameterValue(parameterName, ip);
   }

   private String getParameterName(InjectionPoint ip)
   {
      String parameterName = ip.getAnnotated().getAnnotation(HttpParam.class).value();
      if ("".equals(parameterName))
      {
         parameterName = ip.getMember().getName();
      }
      return parameterName;
   }

   private String getParameterValue(String parameterName, InjectionPoint ip)
   {
      return isParameterInRequest(parameterName) ? request.getParameter(parameterName) : getDefaultValue(ip);
   }

   private boolean isParameterInRequest(String parameterName)
   {
      return request.getParameterMap().containsKey(parameterName);
   }

   private String getDefaultValue(InjectionPoint ip)
   {
      DefaultValue defaultValueAnnotation = ip.getAnnotated().getAnnotation(DefaultValue.class);
      return defaultValueAnnotation == null ? null : defaultValueAnnotation.value();
   }
}
