package org.jboss.seam.faces.event;

import javax.enterprise.event.Observes;
import javax.servlet.http.HttpSessionBindingEvent;

import org.jboss.seam.servlet.event.qualifier.AttributeAdded;

public class EventObserver
{

   public static boolean sessionAttributeAdded;

   public void observeSessionAttributeAdded(@Observes @AttributeAdded HttpSessionBindingEvent e)
   {
      sessionAttributeAdded = true;
   }

}
