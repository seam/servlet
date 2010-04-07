/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
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
package org.jboss.seam.servlet;

import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.conversation.ConversationManager;
import org.jboss.weld.servlet.ServletLifecycle;

/**
 * 
 * @author Nicklas Karlsson
 *
 */
public abstract class ContextualHttpRequest
{
   private HttpServletRequest request;
   private ServletLifecycle lifecycle;
   private ConversationManager conversationManager;

   public ContextualHttpRequest(HttpServletRequest request)
   {
      this.request = request;
      lifecycle = new ServletLifecycle(Container.instance().services().get(ContextLifecycle.class));
   }

   public void run()
   {
      try
      {
         setup();
         process();
      }
      finally
      {
         tearDown();
      }
   }

   private void setup()
   {
      lifecycle.beginRequest(request);
      String cid = request.getParameter("cid");
      conversationManager.beginOrRestoreConversation(cid);
   }

   private void tearDown()
   {
      conversationManager.cleanupConversation();
      lifecycle.endRequest(request);
   }

   protected abstract void process();
}
