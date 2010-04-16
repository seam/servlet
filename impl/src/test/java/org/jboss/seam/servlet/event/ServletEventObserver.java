package org.jboss.seam.servlet.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncEvent;
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

@ApplicationScoped
public class ServletEventObserver
{
   private Map<String, List<Object>> observations = new HashMap<String, List<Object>>();

   private void recordObservation(String id, Object observation)
   {
      List<Object> observed = observations.get(id);
      if (observed == null)
      {
         observed = new ArrayList<Object>();
         observations.put(id, observed);
      }
      observed.add(observation);
   }

   public void reset()
   {
      observations.clear();
   }

   public void assertObservations(String id, Object... observations)
   {
      List<Object> observed = this.observations.get(id);
      assert observed != null && observed.size() == observations.length;
      assert observed.containsAll(Arrays.asList(observations));
   }

   public void observe1(@Observes HttpSessionEvent e)
   {
      recordObservation("1", e);
   }

   public void observe2(@Observes @DidActivate HttpSessionEvent e)
   {
      recordObservation("2", e);
   }

   public void observe3(@Observes @WillPassivate HttpSessionEvent e)
   {
      recordObservation("3", e);
   }

   public void observe4(@Observes @Created HttpSessionEvent e)
   {
      recordObservation("4", e);
   }

   public void observe5(@Observes @Destroyed HttpSessionEvent e)
   {
      recordObservation("5", e);
   }

   public void observe6(@Observes HttpSessionBindingEvent e)
   {
      recordObservation("6", e);
   }

   public void observe7(@Observes @Added HttpSessionBindingEvent e)
   {
      recordObservation("7", e);
   }

   public void observe8(@Observes @Replaced HttpSessionBindingEvent e)
   {
      recordObservation("8", e);
   }

   public void observe9(@Observes @Removed HttpSessionBindingEvent e)
   {
      recordObservation("9", e);
   }

   public void observe10(@Observes @Bound HttpSessionBindingEvent e)
   {
      recordObservation("10", e);
   }

   public void observe11(@Observes @Unbound HttpSessionBindingEvent e)
   {
      recordObservation("11", e);
   }

   public void observe12(@Observes @Added @Attribute("foo") HttpSessionBindingEvent e)
   {
      recordObservation("12", e);
   }

   public void observe13(@Observes @Replaced @Attribute("foo") HttpSessionBindingEvent e)
   {
      recordObservation("13", e);
   }

   public void observe14(@Observes @Removed @Attribute("foo") HttpSessionBindingEvent e)
   {
      recordObservation("14", e);
   }

   public void observe14a(@Observes @Attribute("foo") HttpSessionBindingEvent e)
   {
      recordObservation("14a", e);
   }

   public void observe15(@Observes @Bound @Value("foo") HttpSessionBindingEvent e)
   {
      recordObservation("15", e);
   }

   public void observe16(@Observes @Unbound @Value("foo") HttpSessionBindingEvent e)
   {
      recordObservation("16", e);
   }

   public void observe16a(@Observes @Value("foo") HttpSessionBindingEvent e)
   {
      recordObservation("16a", e);
   }

   public void observe17(@Observes ServletContextEvent e)
   {
      recordObservation("17", e);
   }

   public void observe18(@Observes @Initialized ServletContextEvent e)
   {
      recordObservation("18", e);
   }

   public void observe19(@Observes @Destroyed ServletContextEvent e)
   {
      recordObservation("19", e);
   }

   public void observe20(@Observes ServletContextAttributeEvent e)
   {
      recordObservation("20", e);
   }

   public void observe21(@Observes @Added ServletContextAttributeEvent e)
   {
      recordObservation("21", e);
   }

   public void observe22(@Observes @Replaced ServletContextAttributeEvent e)
   {
      recordObservation("22", e);
   }

   public void observe23(@Observes @Removed ServletContextAttributeEvent e)
   {
      recordObservation("23", e);
   }

   public void observe24(@Observes @Added @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("24", e);
   }

   public void observe25(@Observes @Replaced @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("25", e);
   }

   public void observe26(@Observes @Removed @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("26", e);
   }

   public void observe27(@Observes ServletRequestEvent e)
   {
      recordObservation("27", e);
   }

   public void observe28(@Observes @Initialized ServletRequestEvent e)
   {
      recordObservation("28", e);
   }

   public void observe29(@Observes @Destroyed ServletRequestEvent e)
   {
      recordObservation("29", e);
   }

   public void observe30(@Observes @Added ServletRequestAttributeEvent e)
   {
      recordObservation("30", e);
   }

   public void observe31(@Observes @Replaced ServletRequestAttributeEvent e)
   {
      recordObservation("31", e);
   }

   public void observe32(@Observes @Removed ServletRequestAttributeEvent e)
   {
      recordObservation("32", e);
   }

   public void observe33(@Observes @Added @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("33", e);
   }

   public void observe34(@Observes @Replaced @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("34", e);
   }

   public void observe35(@Observes @Removed @Attribute("foo") ServletRequestAttributeEvent e)
   {
      recordObservation("35", e);
   }

   public void observe36(@Observes @Completed AsyncEvent e)
   {
      recordObservation("36", e);
   }

   public void observe37(@Observes @Error AsyncEvent e)
   {
      recordObservation("37", e);
   }

   public void observe38(@Observes @StartAsync AsyncEvent e)
   {
      recordObservation("38", e);
   }

   public void observe39(@Observes @Timeout AsyncEvent e)
   {
      recordObservation("39", e);
   }
   
   public void observe40(@Observes AsyncEvent e)
   {
      recordObservation("40", e);
   }


}
