package org.jboss.seam.servlet.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Qualifies observer method parameters to select events that fire when
 * attributes being added.
 * 
 * The event parameter can be a {@link HttpSessionBindingEvent},
 * {@link ServletContextAttributeEvent} or a
 * {@link ServletRequestAttributeEvent}.
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target( { FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface AttributeAdded
{
}