package com.alltobs.hj212.validator;


import com.alltobs.hj212.validator.field.ValidDateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ChenQi
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(FieldMissing.List.class)
@Constraint(validatedBy = ValidDateValidator.class)
public @interface FieldMissing {

    boolean cp() default false;

    String message() default "invalid date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean optional() default false;

    /**
     * Defines several {@link FieldMissing} annotations on the same element.
     *
     * @see FieldMissing
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldMissing[] value();
    }
}
