package tv.isshoni.mishima.annotation.exception;

import tv.isshoni.mishima.http.HTTPStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusCode {

    HTTPStatus value();
}
