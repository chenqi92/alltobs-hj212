package com.alltobs.hj212.validator.field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author ChenQi
 */
public class NValidator implements ConstraintValidator<N, String> {

    private int int_len_max;
    private int fraction_len_max;
    private double min;
    private double max;
    private boolean optional;

    @Override
    public void initialize(N n) {
        this.int_len_max = n.integer();
        this.fraction_len_max = n.fraction();
        this.min = n.min();
        this.max = n.max();
        this.optional = n.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return optional;
        }

        try {
            BigDecimal decimal = new BigDecimal(value);
            int int_len = getNumLength(decimal.longValue());
            int fraction_len = decimal.scale();
            boolean result = int_len <= int_len_max &&
                    fraction_len <= fraction_len_max;

            if (min > 0 &&
                    decimal.doubleValue() < min) {
                return false;
            }
            if (max > 0 &&
                    decimal.doubleValue() > max) {
                return false;
            }
            return result;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int getNumLength(long num) {
        num = num > 0 ? num : -num;
        if (num == 0) {
            return 1;
        }
        return (int) Math.log10(num) + 1;
    }
}
