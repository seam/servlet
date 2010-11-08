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

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.jboss.weld.extensions.beanManager.BeanManagerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicklas Karlsson
 */
public class AbstractServletEventBridge extends BeanManagerAware
{
   private transient Logger log = LoggerFactory.getLogger(AbstractServletEventBridge.class);

   protected void fireEvent(final Object payload, final Annotation... qualifiers)
   {
      if (isBeanManagerAvailable())
      {
         /*
          * We can't always guarantee the BeanManager will be available, especially in environments where we don't
          * control bootstrap order that well (e.g. Servlet containers)
          */
         log.trace("Firing event #0 with qualifiers #1", payload, Arrays.asList(qualifiers));
         getBeanManager().fireEvent(payload, qualifiers);
      }
      else
      {
         log.debug("BeanManager not found. Not sending event " + payload + " with qualifiers " + Arrays.asList(qualifiers));
      }
   }
}
