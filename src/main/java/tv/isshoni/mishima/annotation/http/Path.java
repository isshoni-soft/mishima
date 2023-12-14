package tv.isshoni.mishima.annotation.http;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.mishima.annotation.processor.http.PathProcessor;
import tv.isshoni.winry.internal.annotation.processor.type.BootstrapClassProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Weight(
        value = Path.DEFAULT_WEIGHT,
        dynamic = "weight"
)
@Processor({PathProcessor.class, BootstrapClassProcessor.class})
public @interface Path {

    int DEFAULT_WEIGHT = 2147483146;

    String value() default "/";

    int weight() default DEFAULT_WEIGHT;
}
