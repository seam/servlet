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
package org.jboss.seam.servlet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.servlet.http.CookieParam;
import org.jboss.seam.servlet.http.CookieParamProducer;
import org.jboss.seam.servlet.http.HeaderParam;
import org.jboss.seam.servlet.http.HeaderParamProducer;
import org.jboss.seam.servlet.http.RequestParam;
import org.jboss.seam.servlet.http.RequestParamProducer;
import org.jboss.seam.servlet.http.TypedParamValue;
import org.jboss.seam.servlet.util.Primitives;
import org.jboss.weld.extensions.literal.AnyLiteral;
import org.jboss.weld.extensions.literal.DefaultLiteral;

/**
 * @author <a href="mailto:dan.j.allen@gmail.com">Dan Allen</a>
 */
public class ServletExtension implements Extension
{
   private final Map<Class<? extends Annotation>, TypedParamProducerBlueprint> producerBlueprints;
   private final Map<Class<?>, Member> converterMembersByType;
   
   ServletExtension()
   {
      producerBlueprints = new HashMap<Class<? extends Annotation>, TypedParamProducerBlueprint>();
      producerBlueprints.put(RequestParam.class, new TypedParamProducerBlueprint(RequestParamLiteral.INSTANCE));
      producerBlueprints.put(HeaderParam.class, new TypedParamProducerBlueprint(HeaderParamLiteral.INSTANCE));
      producerBlueprints.put(CookieParam.class, new TypedParamProducerBlueprint(CookieParamLiteral.INSTANCE));
      converterMembersByType = new HashMap<Class<?>, Member>();
   }
   
   public Member getConverterMember(Class<?> type)
   {
      return converterMembersByType.get(type);
   }
   
   void detectRequestParamProducer(@Observes ProcessProducerMethod<RequestParamProducer, Object> event)
   {
      if (event.getAnnotatedProducerMethod().isAnnotationPresent(TypedParamValue.class))
      {
         producerBlueprints.get(RequestParam.class).setProducer(event.getBean());
      }
   }
   
   void detectHeaderParamProducer(@Observes ProcessProducerMethod<HeaderParamProducer, Object> event)
   {
      if (event.getAnnotatedProducerMethod().isAnnotationPresent(TypedParamValue.class))
      {
         producerBlueprints.get(HeaderParam.class).setProducer(event.getBean());
      }
   }
   
   void detectCookieParamProducer(@Observes ProcessProducerMethod<CookieParamProducer, Object> event)
   {
      if (event.getAnnotatedProducerMethod().isAnnotationPresent(TypedParamValue.class))
      {
         producerBlueprints.get(CookieParam.class).setProducer(event.getBean());
      }
   }
   
   <X> void detectInjections(@Observes ProcessInjectionTarget<X> event)
   {
      for (InjectionPoint ip : event.getInjectionTarget().getInjectionPoints())
      {
         Annotated annotated = ip.getAnnotated();
         for (Class<? extends Annotation> paramAnnotationType : producerBlueprints.keySet())
         {
            if (annotated.isAnnotationPresent(paramAnnotationType))
            {
               Collection<Annotation> allowed = Arrays.asList(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE, annotated.getAnnotation(paramAnnotationType));
               boolean error = false;
               for (Annotation q : ip.getQualifiers())
               {
                  if (!allowed.contains(q))
                  {
                     event.addDefinitionError(new IllegalArgumentException("Additional qualifiers not permitted on @" + paramAnnotationType.getSimpleName() + " injection point: " + ip));
                     error = true;
                     break;
                  }
               }
               if (error)
               {
                  break;
               }
               Type targetType = getActualBeanType(ip.getType());
               if (!(targetType instanceof Class))
               {
                  event.addDefinitionError(new IllegalArgumentException("@" + paramAnnotationType.getSimpleName() + " injection point must be a raw type: " + ip));
                  break;
               }
               try
               {
                  Class<?> targetClass = (Class<?>) targetType;
                  if (!targetClass.equals(String.class))
                  {
                     targetClass = Primitives.wrap(targetClass);
                     Member converter = null;
                     try
                     {
                        converter = targetClass.getConstructor(String.class);
                     }
                     catch (NoSuchMethodException sce)
                     {
                        converter = targetClass.getMethod("valueOf", String.class);
                     }
                     // TODO need way to register or detect custom converters
                     converterMembersByType.put(targetClass, converter);
                  }
                  producerBlueprints.get(paramAnnotationType).addTargetType(targetClass);
               }
               catch (NoSuchMethodException nme)
               {
                  event.addDefinitionError(new IllegalArgumentException("No converter available for type at @" + paramAnnotationType.getName() + " injection point: " + ip));
               }
            }
         }
      }
   }
   
   void installBeans(@Observes AfterBeanDiscovery event, BeanManager beanManager)
   {
      for (TypedParamProducerBlueprint blueprint : producerBlueprints.values())
      {
         if (blueprint.getProducer() != null)
         {
            for (Class<?> type : blueprint.getTargetTypes())
            {
               event.addBean(createdTypedParamProducer(blueprint.getProducer(), type, blueprint.getQualifier(), beanManager));
            }
         }
      }
     
      producerBlueprints.clear();
   }
   
   private <T> Bean<T> createdTypedParamProducer(Bean<Object> delegate, Class<T> targetType, Annotation qualifier, BeanManager beanManager)
   {
      return new NarrowingBeanBuilder<T>(delegate, beanManager)
            .readFromType(beanManager.createAnnotatedType(targetType)).qualifiers(qualifier).create();
   }
   
   private Type getActualBeanType(Type t)
   {
      if (t instanceof ParameterizedType && ((ParameterizedType) t).getRawType().equals(Instance.class))
      {
         return ((ParameterizedType) t).getActualTypeArguments()[0];
      }
      return t;
   }

   public static class TypedParamProducerBlueprint
   {
      private Bean<Object> producer;
      private Set<Class<?>> targetTypes;
      private Annotation qualifier;
      
      public TypedParamProducerBlueprint(Annotation qualifier)
      {
         this.qualifier = qualifier;
         targetTypes = new HashSet<Class<?>>();
      }
      
      public Bean<Object> getProducer()
      {
         return producer;
      }
      
      public void setProducer(Bean<Object> producer)
      {
         this.producer = producer;
      }
      
      public Set<Class<?>> getTargetTypes()
      {
         return targetTypes;
      }
      
      public void addTargetType(Class<?> targetType)
      {
         targetTypes.add(targetType);
      }
      
      public Annotation getQualifier()
      {
         return qualifier;
      }
   }
   
   // TODO move me to top-level type
   public static class RequestParamLiteral extends AnnotationLiteral<RequestParam> implements RequestParam
   {
      private final String value;
      
      public RequestParamLiteral()
      {
         this("");
      }
      
      public RequestParamLiteral(String value)
      {
         this.value = value;
      }
      
      public String value()
      {
         return value;
      }
      
      public static final RequestParamLiteral INSTANCE = new RequestParamLiteral();
   }
   
   // TODO move me to top-level type
   public static class HeaderParamLiteral extends AnnotationLiteral<HeaderParam> implements HeaderParam
   {
      private final String value;
      
      public HeaderParamLiteral()
      {
         this("");
      }
      
      public HeaderParamLiteral(String value)
      {
         this.value = value;
      }
      
      public String value()
      {
         return value;
      }
      
      public static final HeaderParamLiteral INSTANCE = new HeaderParamLiteral();
   }
   
   public static class CookieParamLiteral extends AnnotationLiteral<CookieParam> implements CookieParam
   {
      private final String value;
      
      public CookieParamLiteral()
      {
         this("");
      }
      
      public CookieParamLiteral(String value)
      {
         this.value = value;
      }
      
      public String value()
      {
         return value;
      }
      
      public static final CookieParamLiteral INSTANCE = new CookieParamLiteral();
   }
}
