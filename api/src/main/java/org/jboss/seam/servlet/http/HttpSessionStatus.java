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
package org.jboss.seam.servlet.http;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A helper bean that can be injected to check the status of the HttpSession and acquiring it
 * 
 * @author Nicklas Karlsson
 */
@RequestScoped
public class HttpSessionStatus
{
   @Inject
   private HttpServletRequest request;

   public boolean isActive()
   {
      HttpSession session = request.getSession(false);
      try
      {
         return session != null && (session.getMaxInactiveInterval() > 0);
      }
      catch (IllegalStateException e)
      {
         return false;
      }
   }

   public HttpSession getOrCreate()
   {
      return request.getSession();
   }
}
