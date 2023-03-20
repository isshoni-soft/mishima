package tv.isshoni.mishima.annotation.http.parameter;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.mishima.annotation.processor.http.parameter.PathProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Weight(5)
@Processor(PathProcessor.class)
public @interface Path {

    String value();
}
