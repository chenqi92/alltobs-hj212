package com.alltobs.hj212.validator.field;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串
 *
 * @author ChenQi
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(C.List.class)
@Constraint(validatedBy = CValidator.class)
public @interface C {

    /**
     * @return 最大长度
     */
    int len();

    String message() default "invalid C type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link C} annotations on the same element.
     *
     * @see C
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        C[] value();
    }
}
