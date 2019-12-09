package com.faos7.tickets.constraints;

import com.faos7.tickets.validator.RequestRouteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = RequestRouteValidator.class)
@Documented
public @interface  CustomRoute {
    String message() default "{route.not.found}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
