package org.jboss.seam.servlet.event;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.jboss.weld.extensions.beanManager.BeanManagerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractServletEventBridge extends BeanManagerAware
{

   private transient Logger log = LoggerFactory.getLogger(AbstractServletEventBridge.class);

   protected void fireEvent(final Object payload, final Annotation... qualifiers)
   {
      if (isBeanManagerAvailable())
      {
         /*
          * We can't always guarantee the BeanManager will be available,
          * especially in environemnts where we don't control bootstrap order
          * that well (e.g. Servlet containers)
          */
         log.trace("Firing event #0 with qualifiers #1", payload, Arrays.asList(qualifiers));
         getBeanManager().fireEvent(payload, qualifiers);
      }
      else
      {
         log.debug("BeanManager can't be found so not sending event " + payload + " with qualifiers " + Arrays.asList(qualifiers));
      }
   }

}