package com.alltobs.hj212.validator.clazz;


import com.alltobs.hj212.validator.field.C;

import javax.validation.Constraint;
import javax.validation.Payload;
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
@Repeatable(FieldC.List.class)
@Constraint(validatedBy = T212MapCValidator.class)
public @interface FieldC {

    String field() default "";

    C value();

    String message() default "invalid C type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean optional() default false;

    boolean regex() default false;

    /**
     * Defines several {@link FieldC} annotations on the same element.
     *
     * @see FieldC
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldC[] value();
    }
}
