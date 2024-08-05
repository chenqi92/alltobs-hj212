package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import com.alltobs.hj212.translator.ValueMatch;
import com.alltobs.hj212.translator.ValuePattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 命令类别
 *
 * @author ChenQi
 */
@Getter
@AllArgsConstructor
public enum CommandType implements ValueLabel, ValueMatch, ValuePattern {

    REQUEST("请求命令", "[1]\\d{3}"),
    UPLOAD("上传命令", "[2]\\d{3}"),
    NOTICE("通知命令", "[3]\\d{3}"),
    SHELL("交互命令", "[9]\\d{3}"),
    UNKNOW("未知", "\\d{4}", 10000);

    private final String value;
    private final String label;
    private final String pattern;
    private final Predicate<String> predicate;
    private final int order;

    CommandType(String label, String pattern) {
        this.value = name();
        this.label = label;
        this.pattern = pattern;
        this.predicate = Pattern.compile(this.pattern).asPredicate();
        this.order = ordinal();
    }

    CommandType(String label, String pattern, int order) {
        this.value = name();
        this.label = label;
        this.pattern = pattern;
        this.predicate = Pattern.compile(this.pattern).asPredicate();
        this.order = order;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String pattern() {
        return pattern;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public boolean match(String value) {
        return predicate.test(value);
    }

}
