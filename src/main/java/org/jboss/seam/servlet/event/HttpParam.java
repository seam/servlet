package org.jboss.seam.servlet.event;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualifies injection points that should have their values fetched from a HTTP request attribute
 * 
 * @author Nicklas Karlsson
 *
 */
@Qualifier
@Retention(RUNTIME)
@Target( { FIELD, PARAMETER, METHOD })
public @interface HttpParam
{
   @Nonbinding
   public String value();
}