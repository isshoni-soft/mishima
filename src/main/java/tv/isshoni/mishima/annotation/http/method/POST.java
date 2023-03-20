package tv.isshoni.mishima.annotation.http.method;

import tv.isshoni.araragi.annotation.Processor;
import tv.isshoni.araragi.annotation.Weight;
import tv.isshoni.mishima.annotation.processor.http.method.POSTProcessor;
import tv.isshoni.mishima.http.MIMEType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Weight(500000000)
@Processor(POSTProcessor.class)
public @interface POST {
    String value(); // path

    MIMEType resultType() default MIMEType.TEXT;
}
