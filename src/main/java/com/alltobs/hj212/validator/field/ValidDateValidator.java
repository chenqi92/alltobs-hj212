package com.alltobs.hj212.validator.field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 * Created by xiaoyao9184 on 2018/1/10.
 */
public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private String format;
    private boolean optional;

    @Override
    public void initialize(ValidDate validDate) {
        this.format = validDate.format();
        this.optional = validDate.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return optional;
        }
        return isValidFormat(format, value);
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (value != null) {
                date = sdf.parse(value);
                if (!value.equals(sdf.format(date))) {
                    date = null;
                }
            }

        } catch (ParseException ignored) {
        }
        return date != null;
    }
}
