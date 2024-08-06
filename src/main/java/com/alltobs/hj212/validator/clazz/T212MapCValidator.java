package com.alltobs.hj212.validator.clazz;

import com.alltobs.hj212.model.verify.T212Map;
import com.alltobs.hj212.validator.field.C;
import com.alltobs.hj212.validator.field.CValidator;

import jakarta.validation.ConstraintValidator;

/**
 * @author ChenQi
 */
public class T212MapCValidator
        extends T212MapFieldValidator<FieldC, C>
        implements ConstraintValidator<FieldC, T212Map<String, ?>> {

    public T212MapCValidator() {
        super(new CValidator());
    }

    @Override
    public String getField(FieldC fieldC) {
        return fieldC.field();
    }

    @Override
    public C getAnnotation(FieldC fieldC) {
        return fieldC.value();
    }

    @Override
    public boolean isFieldRegex(FieldC fieldC) {
        return fieldC.regex();
    }

    @Override
    public String getFieldMessage(C value) {
        return value.message();
    }

}
