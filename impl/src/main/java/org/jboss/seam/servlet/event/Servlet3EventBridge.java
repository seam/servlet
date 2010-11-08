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
package org.jboss.seam.servlet.event;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.jboss.seam.servlet.event.qualifier.literal.CompletedLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.ErrorLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.StartAsyncLiteral;
import org.jboss.seam.servlet.event.qualifier.literal.TimeoutLiteral;

/**
 * A servlet listener that propagates the events to the current CDI Bean Manager
 * event queue.
 * 
 * This interface handles listeners that were added in Servlet 3 to preserve
 * compatibility with Servlet 2.5
 * 
 * @author Nicklas Karlsson
 */
public class Servlet3EventBridge extends AbstractServletEventBridge implements AsyncListener
{
   /**
    * Asynchronous events
    */

   public void onComplete(final AsyncEvent e) throws IOException
   {
      fireEvent(e, CompletedLiteral.INSTANCE);
   }

   public void onError(final AsyncEvent e) throws IOException
   {
      fireEvent(e, ErrorLiteral.INSTANCE);
   }

   public void onStartAsync(final AsyncEvent e) throws IOException
   {
      fireEvent(e, StartAsyncLiteral.INSTANCE);
   }

   public void onTimeout(final AsyncEvent e) throws IOException
   {
      fireEvent(e, TimeoutLiteral.INSTANCE);
   }
}
