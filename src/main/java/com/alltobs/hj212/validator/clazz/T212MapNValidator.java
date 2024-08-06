package com.alltobs.hj212.validator.clazz;

import com.alltobs.hj212.model.verify.T212Map;
import com.alltobs.hj212.validator.field.N;
import com.alltobs.hj212.validator.field.NValidator;

import jakarta.validation.ConstraintValidator;

/**
 * @author ChenQi
 */
public class T212MapNValidator
        extends T212MapFieldValidator<FieldN, N>
        implements ConstraintValidator<FieldN, T212Map<String, ?>> {

    public T212MapNValidator() {
        super(new NValidator());
    }

    @Override
    public String getField(FieldN fieldN) {
        return fieldN.field();
    }

    @Override
    public N getAnnotation(FieldN fieldN) {
        return fieldN.value();
    }

    @Override
    public boolean isFieldRegex(FieldN fieldN) {
        return fieldN.regex();
    }

    @Override
    public String getFieldMessage(N value) {
        return value.message();
    }
}
