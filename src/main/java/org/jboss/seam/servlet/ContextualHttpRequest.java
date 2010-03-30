package org.jboss.seam.servlet;

import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.conversation.ConversationManager;
import org.jboss.weld.servlet.ServletLifecycle;

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
