package tv.isshoni.mishima.annotation.http;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.winry.internal.annotation.processor.type.BootstrapClassProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Weight(
        value = Overseer.DEFAULT_WEIGHT,
        dynamic = "value"
)
@Processor(BootstrapClassProcessor.class)
public @interface Overseer {
    int DEFAULT_WEIGHT = 2147483146;

    int value() default DEFAULT_WEIGHT;
}
