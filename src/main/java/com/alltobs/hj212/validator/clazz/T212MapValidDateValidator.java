package com.alltobs.hj212.validator.clazz;

import com.alltobs.hj212.model.verify.T212Map;
import com.alltobs.hj212.validator.field.ValidDate;
import com.alltobs.hj212.validator.field.ValidDateValidator;

import jakarta.validation.ConstraintValidator;

/**
 * @author Created by xiaoyao9184 on 2018/1/10.
 */
public class T212MapValidDateValidator
        extends T212MapFieldValidator<FieldValidDate, ValidDate>
        implements ConstraintValidator<FieldValidDate, T212Map<String, ?>> {

    public T212MapValidDateValidator() {
        super(new ValidDateValidator());
    }

    @Override
    public String getField(FieldValidDate field) {
        return field.field();
    }

    @Override
    public ValidDate getAnnotation(FieldValidDate field) {
        return field.value();
    }

    @Override
    public boolean isFieldRegex(FieldValidDate field) {
        return field.regex();
    }

    @Override
    public String getFieldMessage(ValidDate value) {
        return value.message();
    }
}
