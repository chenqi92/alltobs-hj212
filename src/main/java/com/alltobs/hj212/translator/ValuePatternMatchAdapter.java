package com.alltobs.hj212.translator;

import java.util.regex.Pattern;

/**
 * @author ChenQi
 */
public class ValuePatternMatchAdapter implements ValueMatch {

    public static ValuePatternMatchAdapter adaptation(ValuePattern valuePattern) {
        ValuePatternMatchAdapter adapter = new ValuePatternMatchAdapter();
        adapter.valuePattern = valuePattern;
        adapter.pattern = Pattern.compile(valuePattern.pattern());
        return adapter;
    }

    private ValuePattern valuePattern;
    private Pattern pattern;

    @Override
    public boolean match(String code) {
        return pattern.asPredicate().test(code);
    }

    @Override
    public String pattern() {
        return valuePattern.pattern();
    }

    @Override
    public int order() {
        return -1;
    }

    @Override
    public String value() {
        return valuePattern.value();
    }

    @Override
    public String label() {
        return valuePattern.label();
    }
}
