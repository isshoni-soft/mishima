package tv.isshoni.mishima.annotation.http.method;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.mishima.annotation.processor.http.method.DELETEProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Weight(190000)
@Processor(DELETEProcessor.class)
public @interface DELETE {
    String value(); // path
}
