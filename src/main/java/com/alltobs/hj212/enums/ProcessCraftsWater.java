package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
public enum ProcessCraftsWater implements ValueLabel {

    _1("污水处理厂进口污水流量及污染物", null, "1"),
    _2("污水处理厂出口污水流量及污染物", null, "2"),
    _3(null, "传统活性污泥法", "3"),
    _4(null, "氧化沟法", "4"),
    _5(null, "AO 法—A2O 法", "5"),
    _6(null, "SBR 法", "6"),
    _7(null, "生物接触氧化法", "7"),
    _8(null, "生物滤池法", "8"),
    _9("污水处理厂设计参数", null, "9"),
    _10("预留扩充", null, "a-b");

    private final String code;
    private final String meaning;
    private final String type;

    ProcessCraftsWater(String type, String meaning, String code) {
        this.code = code;
        if (meaning == null) {
            this.meaning = type;
        } else {
            this.meaning = meaning;
        }
        this.type = type;
    }


    @Override
    public String value() {
        return code;
    }

    @Override
    public String label() {
        return meaning;
    }
}
