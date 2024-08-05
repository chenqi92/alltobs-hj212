package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import com.alltobs.hj212.translator.ValueMatch;
import com.alltobs.hj212.translator.ValuePattern;
import lombok.Getter;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author ChenQi
 */
@Getter
public enum SystemType implements ValueLabel, ValueMatch, ValuePattern {

    _10_29("环境质量类别", "[12]\\d"),
    _30_49("环境污染源类别", "[34]\\d"),
    _50_69("工况类别", "[56]\\d"),
    _91_99("系统交互类别", "[9][1-9]"),
    _A0_Z9("于未知系统编码扩展", "[A-Z][A-Z0-9]|[A-Z0-9][A-Z]"),
    _UNKNOW("未知", "[0-9A-Z]{2}", 10000);

    private final String code;
    private final String meaning;
    private final String pattern;
    private final Predicate<String> predicate;
    private final int order;

    SystemType(String meaning, String pattern) {
        this.code = name().substring(1);
        this.meaning = meaning;
        this.pattern = pattern;
        this.predicate = Pattern.compile(this.pattern).asPredicate();
        this.order = ordinal();
    }

    SystemType(String meaning, String pattern, int order) {
        this.code = name().substring(1);
        this.meaning = meaning;
        this.pattern = pattern;
        this.predicate = Pattern.compile(this.pattern).asPredicate();
        this.order = order;
    }

    @Override
    public String value() {
        return code;
    }

    @Override
    public String label() {
        return meaning;
    }

    @Override
    public String pattern() {
        return pattern;
    }

    @Override
    public boolean match(String code) {
        return predicate.test(code);
    }

    @Override
    public int order() {
        return order;
    }
}
