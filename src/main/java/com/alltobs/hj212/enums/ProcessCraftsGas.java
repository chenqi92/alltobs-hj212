package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
public enum ProcessCraftsGas implements ValueLabel {

    _1("脱硫设施", "湿法脱硫（石灰石/石灰-石膏法）", "1"),
    _2("脱硫设施", "半干法脱硫（循环硫化床法）", "2"),
    _3("脱硝设施", "SCR", "3"),
    _4("脱硝设施", "SNCR", "4"),
    _5("除尘", "电除尘", "5"),
    _6("除尘", "布袋除尘", "6"),
    _10("预留扩充", "", "a-b");

    private final String code;
    private final String meaning;
    private final String type;

    ProcessCraftsGas(String type, String meaning, String code) {
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
