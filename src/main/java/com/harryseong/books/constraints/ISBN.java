package com.harryseong.books.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by harry on 4/17/17.
 */

@Constraint(validatedBy = ISBNValidator.class)  // ISBNValidator contains the validation logic
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ISBN {

    String message() default "Invalid ISBN";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}