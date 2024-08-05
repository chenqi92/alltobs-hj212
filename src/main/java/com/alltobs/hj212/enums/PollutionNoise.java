package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
public enum PollutionNoise implements ValueLabel {

    LA("A权声级", "分贝", "N3.1"),
    L5("累计百分声级L5", "分贝", "N3.1"),
    L10("累计百分声级L10", "分贝", "N3.1"),
    L50("累计百分声级L50", "分贝", "N3.1"),
    L90("累计百分声级L90", "分贝", "N3.1"),
    L95("累计百分声级L95", "分贝", "N3.1"),
    Leq("等效声级", "分贝", "N3.1"),
    Ldn("昼夜等效声级", "分贝", "N3.1"),
    Ld("昼间等效声级", "分贝", "N3.1"),
    Ln("夜间等效声级", "分贝", "N3.1"),
    LMx("最大的瞬时声级", "分贝", "N3.1"),
    LMn("最小的瞬时声级", "分贝", "N3.1"),
    ;

    private final String code;
    private final String meaning;
    private final String unit;
    private final String type;

    PollutionNoise(String meaning, String unit, String type) {
        this.code = name();
        this.meaning = meaning;
        this.unit = unit;
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
