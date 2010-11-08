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

import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.conversation.ConversationManager;
import org.jboss.weld.servlet.ServletLifecycle;

/**
 * @author Nicklas Karlsson
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
