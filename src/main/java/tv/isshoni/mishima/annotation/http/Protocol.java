package tv.isshoni.mishima.annotation.http;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.mishima.http.protocol.ProtocolProcessor;
import tv.isshoni.winry.internal.annotation.processor.type.BootstrapClassProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Weight(2147483147)
@Processor({BootstrapClassProcessor.class, ProtocolProcessor.class})
public @interface Protocol {

    String value();
}
