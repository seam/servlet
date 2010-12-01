package org.jboss.seam.servlet.event;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.weld.extensions.beanManager.BeanManagerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class that handles sending events to the CDI event bus with support
 * for environments where injection into the Servlet component is not available.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public abstract class AbstractServletEventBridge extends BeanManagerAware
{
   private transient Logger log = LoggerFactory.getLogger(AbstractServletEventBridge.class);
   
   @Inject
   private BeanManager beanManager;
   
   /**
    * Propogates the Servlet event to the CDI event bus if the BeanManager is available.
    * If injection is available, this will always be skipped, and thus the performance optimal
    */
   protected void fireEvent(final Object payload, final Annotation... qualifiers)
   {
      if (beanManager == null)
      {
         try
         {
            beanManager = getBeanManager();
         }
         catch (IllegalStateException e)
         {
            log.info("CDI BeanManager could not be found. Not sending event " + payload + " with qualifiers " + Arrays.asList(qualifiers));
            return;
         }
      }
      
      beanManager.fireEvent(payload, qualifiers);
   }
}
