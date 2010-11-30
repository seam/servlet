package org.jboss.seam.servlet.messages;

import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Message;
import org.jboss.weld.extensions.log.MessageBundle;

@MessageBundle
public interface ServletMessages
{
   @Message("Additional qualifiers not permitted at @%s injection point: %s")
   String additionalQualifiersNotPermitted(String injectionPointName, InjectionPoint ip);
   
   @Message("@%s injection point must be a raw type: %s")
   String rawTypeRequired(String injectionPointName, InjectionPoint ip);
   
   @Message("No converter available for type at @%s injection point: %s")
   String noConverterForType(String injectionPointName, InjectionPoint ip);
}
