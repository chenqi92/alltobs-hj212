package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
public enum LiveSideMessageType implements ValueLabel {

    _1("日志", "1"),
    _2("状态", "2"),
    _3("参数", "3"),
    _4("预留扩充", "4"),
    _5("预留扩充", "5");

    private final String value;
    private final String label;


    LiveSideMessageType(String label, String value) {
        this.value = value;
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
