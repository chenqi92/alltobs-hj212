package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据标记
 *
 * @author ChenQi
 */
@Getter
@AllArgsConstructor
public enum DataFlag implements ValueLabel {

    N("在线监控（监测）仪器仪表工作正常"),
    F("在线监控（监测）仪器仪表停运"),
    M("在线监控（监测）仪器仪表处于维护期间产生的数据"),
    S("手工输入的设定值"),
    D("在线监控（监测）仪器仪表故障"),
    C("在线监控（监测）仪器仪表处于校准状态"),
    T("在线监控（监测）仪器仪表采样数值超过测量上限"),
    B("在线监控（监测）仪器仪表与数采仪通讯异常");

    private final String value;
    private final String label;

    DataFlag(String label) {
        this.value = name();
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
