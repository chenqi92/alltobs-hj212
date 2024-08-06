package com.alltobs.hj212.validator;


import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.verify.DataElement;
import com.alltobs.hj212.model.verify.T212Map;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ChenQi
 */
public class T212MapValueRangeValidator
        implements ConstraintValidator<ValueRange, T212Map> {

    private ValueRange fieldMissing;


    @Override
    public void initialize(ValueRange fieldMissing) {
        this.fieldMissing = fieldMissing;
    }

    @Override
    public boolean isValid(T212Map value, ConstraintValidatorContext constraintValidatorContext) {
        Map<String, String> result = value;

        Stream<DataElement> stream = Stream.of(DataElement.values())
                .filter(DataElement::isRequired);
        if (result.containsKey(DataElement.Flag.name())) {
            String f = result.get(DataElement.Flag.name());
            int flag = Integer.parseInt(f);
            if (HjDataFlag.D.isMarked(flag)) {
                stream = Stream.concat(stream, Stream.of(DataElement.PNO, DataElement.PNUM));
            }
        }

        Optional<DataElement> missing = stream
                .filter(e -> !result.containsKey(e.name()))
                .findFirst();
        if (missing.isPresent()) {
            return false;
        }
        return true;
    }

}
