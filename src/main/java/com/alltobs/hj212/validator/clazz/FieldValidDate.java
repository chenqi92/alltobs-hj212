package com.alltobs.hj212.validator.clazz;

import com.alltobs.hj212.validator.field.ValidDate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author chenqi
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(FieldValidDate.List.class)
@Constraint(validatedBy = T212MapValidDateValidator.class)
public @interface FieldValidDate {

    String field() default "";

    ValidDate value();

    String message() default "invalid date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean optional() default false;

    boolean regex() default false;

    /**
     * Defines several {@link FieldValidDate} annotations on the same element.
     *
     * @see FieldValidDate
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldValidDate[] value();
    }
}
