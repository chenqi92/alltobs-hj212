package com.alltobs.hj212.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;

/**
 * 功能:
 *
 * @author chenQi
 */
@Getter
public enum HjDataFlag {

    /**
     *
     */
    @Schema(title = "命令是否应答")
    A(1),
    @Schema(title = "是否有数据包序号")
    D(2),
    @Schema(title = "标准版本号V0（HJ 212-2017）")
    V0,
    @Schema(title = "标准版本号V1")
    V1,
    @Schema(title = "标准版本号V2")
    V2,
    @Schema(title = "标准版本号V3")
    V3,
    @Schema(title = "标准版本号V4")
    V4,
    @Schema(title = "标准版本号V5")
    V5;

    private int bit;

    HjDataFlag() {
        this.bit = (1 << ordinal());
    }

    HjDataFlag(int bit) {
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }

    public boolean isMarked(int flags) {
        return (flags & bit) != 0;
    }

    public boolean isMarked(Collection<HjDataFlag> flags) {
        return flags != null && flags.contains(this);
    }
}
