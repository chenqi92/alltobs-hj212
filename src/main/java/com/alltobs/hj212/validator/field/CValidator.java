package com.alltobs.hj212.validator.field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ChenQi
 */
public class CValidator implements ConstraintValidator<C, String> {

    private int max;

    @Override
    public void initialize(C validDate) {
        this.max = validDate.len();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        int len;
        if (value == null) {
            len = 0;
        } else {
            len = value.length();
        }
        return len <= max;
    }

}
