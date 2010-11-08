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
package org.jboss.seam.servlet.test.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.servlet.event.qualifier.Destroyed;
import org.jboss.seam.servlet.event.qualifier.DidActivate;
import org.jboss.seam.servlet.event.qualifier.Initialized;
import org.jboss.seam.servlet.event.qualifier.WillPassivate;
import org.junit.Assert;

/**
 * @author Nicklas Karlsson
 * @author <a href="mailto:dan.j.allen@gmail.com">Dan Allen</a>
 */
@ApplicationScoped
public class ServletEventBridgeTestHelper
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

   public Map<String, List<Object>> getObservations()
   {
      return observations;
   }
   
   public void reset()
   {
      observations.clear();
   }

   public void assertObservations(String id, Object... observations)
   {
      List<Object> observed = this.observations.get(id);
      if (observations.length > 0)
      {
         Assert.assertNotNull("Observer [@Observes " + id + "] was never notified", observed);
      }
      else
      {
         return;
      }
      
      //Assert.assertEquals(observations.length, observed.size());
      for (Object o : observations)
      {
         if (!observed.remove(o))
         {
            Assert.fail("Observer [@Observes " + id + "] notified too few times; expected payload: " + o);
         }
      }
      
      if (observed.size() > 0)
      {
         Assert.fail("Observer [@Observes " + id + "] notified too many times; extra payload: " + observed);
      }
   }

   public void observeServletRequest(@Observes ServletRequest req)
   {
      recordObservation("ServletRequest", req);
   }

   public void observeServletRequestInitialized(@Observes @Initialized ServletRequest req)
   {
      recordObservation("@Initialized ServletRequest", req);
   }

   public void observeServletRequestDestroyed(@Observes @Destroyed ServletRequest req)
   {
      recordObservation("@Destroyed ServletRequest", req);
   }
   
   public void observeHttpServletRequest(@Observes HttpServletRequest req)
   {
      recordObservation("HttpServletRequest", req);
   }

   public void observeHttpServletRequestInitialized(@Observes @Initialized HttpServletRequest req)
   {
      recordObservation("@Initialized HttpServletRequest", req);
   }

   public void observeHttpServletRequestDestroyed(@Observes @Destroyed HttpServletRequest req)
   {
      recordObservation("@Destroyed HttpServletRequest", req);
   }
   
   public void observeHttpSession(@Observes HttpSession sess)
   {
      recordObservation("HttpSession", sess);
   }

   public void observeHttpSessionDidActivate(@Observes @DidActivate HttpSession sess)
   {
      recordObservation("@DidActivate HttpSession", sess);
   }

   public void observeSessionWillPassivate(@Observes @WillPassivate HttpSession sess)
   {
      recordObservation("@WillPassivate HttpSession", sess);
   }

   public void observeSessionInitialized(@Observes @Initialized HttpSession sess)
   {
      recordObservation("@Initialized HttpSession", sess);
   }

   public void observeSessionDestroyed(@Observes @Destroyed HttpSession sess)
   {
      recordObservation("@Destroyed HttpSession", sess);
   }

   public void observeServletContext(@Observes ServletContext ctx)
   {
      recordObservation("ServletContext", ctx);
   }

   public void observeServletContextInitialized(@Observes @Initialized ServletContext ctx)
   {
      recordObservation("@Initialized ServletContext", ctx);
   }

   public void observeServletContextDestroyed(@Observes @Destroyed ServletContext ctx)
   {
      recordObservation("@Destroyed ServletContext", ctx);
   }

   /*
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
   */
}
