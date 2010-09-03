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
package org.jboss.seam.servlet.event;

import java.io.IOException;

import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.jboss.seam.servlet.event.qualifier.Added;
import org.jboss.seam.servlet.event.qualifier.Attribute;
import org.jboss.seam.servlet.event.qualifier.Bound;
import org.jboss.seam.servlet.event.qualifier.Completed;
import org.jboss.seam.servlet.event.qualifier.Created;
import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.DidActivate;
import org.jboss.seam.servlet.event.qualifier.Error;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.jboss.seam.servlet.event.qualifier.Removed;
import org.jboss.seam.servlet.event.qualifier.Replaced;
import org.jboss.seam.servlet.event.qualifier.StartAsync;
import org.jboss.seam.servlet.event.qualifier.Timeout;
import org.jboss.seam.servlet.event.qualifier.Unbound;
import org.jboss.seam.servlet.event.qualifier.Value;
import org.jboss.seam.servlet.event.qualifier.WillPassivate;

/**
 * A servlet listener that propagates the events to the current CDI Bean Manager
 * event queue.
 * 
 * This interface handles listeners that were added in Servlet 3 to preserve
 * compatibility with Servlet 2.5
 * 
 * @author Nicklas Karlsson
 * 
 */
public class Servlet3EventBridge extends AbstractServletEventBridge implements AsyncListener
{
   /**
    * Asynchronous events
    */

   public void onComplete(final AsyncEvent e) throws IOException
   {
      fireEvent(e, COMPLETED);
   }

   public void onError(final AsyncEvent e) throws IOException
   {
      fireEvent(e, ERROR);
   }

   public void onStartAsync(final AsyncEvent e) throws IOException
   {
      fireEvent(e, STARTASYNCH);
   }

   public void onTimeout(final AsyncEvent e) throws IOException
   {
      fireEvent(e, TIMEOUT);
   }

   private static final AnnotationLiteral<Completed> COMPLETED = new AnnotationLiteral<Completed>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Error> ERROR = new AnnotationLiteral<Error>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<StartAsync> STARTASYNCH = new AnnotationLiteral<StartAsync>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };

   private static final AnnotationLiteral<Timeout> TIMEOUT = new AnnotationLiteral<Timeout>()
   {
      private static final long serialVersionUID = -1610281796509557441L;
   };
}
