package org.jboss.seam.servlet.event;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.extensions.beanManager.BeanManagerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractServletEventBridge extends BeanManagerAware
{

   private transient Logger log = LoggerFactory.getLogger(AbstractServletEventBridge.class);

   protected void fireEvent(final Object payload, final Annotation... qualifiers)
   {
      // TODO - this should probably be replaced with an isBeanManagerAvailable() method.
      BeanManager beanManager;
      try
      {
         beanManager = getBeanManager();
      }
      catch (Exception e)
      {
         beanManager = null;
      }

      if (beanManager != null)
      {
         /*
          * We can't always guarantee the BeanManager will be available, especially in environments where we don't
          * control bootstrap order that well (e.g. Servlet containers)
          */
         log.trace("Firing event #0 with qualifiers #1", payload, Arrays.asList(qualifiers));
         beanManager.fireEvent(payload, qualifiers);
      }
      else
      {
         log.debug("BeanManager can't be found so not sending event " + payload + " with qualifiers "
                  + Arrays.asList(qualifiers));
      }
   }

}