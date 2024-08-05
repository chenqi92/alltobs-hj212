package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
public enum LiveSideDeviceType implements ValueLabel {

    _1("在线监控（监测）仪器仪表", "1"),
    _2("数据采集传输仪", "2"),
    _3("辅助设备", "3"),
    _4("预留扩充", "4"),
    _5("预留扩充", "5");

    private final String value;
    private final String label;


    LiveSideDeviceType(String label, String value) {
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
