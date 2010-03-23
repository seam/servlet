package org.jboss.seam.servlet.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.servlet.http.HttpSessionEvent;

/**
 * Qualifies observer method parameters to select events that fire when HTTP
 * sessions are created.
 * 
 * The event parameter is an {@link HttpSessionEvent}
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Created {}