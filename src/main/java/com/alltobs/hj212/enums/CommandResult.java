package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
@AllArgsConstructor
public enum CommandResult implements ValueLabel {

    _1("准备执行请求"),
    _2("请求被拒绝"),
    _3("PW 错误"),
    _4("MN 错误"),
    _5("ST 错误"),
    _6("Flag 错误"),
    _7("QN 错误"),
    _8("CN 错误"),
    _9("CRC 校验错误"),
    _100("未知错误");

    private final String value;
    private final String label;

    CommandResult(String label) {
        this.value = name().substring(1);
        this.label = label;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String label() {
        return label;
    }
}
