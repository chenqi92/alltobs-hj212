package com.alltobs.hj212.model;

import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.field.C;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;

/**
 * 功能:
 *
 * @author chenQi
 */
@Data
public class LiveSide {

    @Schema(title = "现场端信息", name = "Info")
    @JsonProperty("Info")
    @JsonbProperty("Info")
    private String info;

    @Schema(title = "在线监控（监测）仪器仪表编码", name = "SN")
    @C(len = 24, groups = VersionGroup.V2017.class)
    @JsonProperty("SN")
    @JsonbProperty("SN")
    private String sn;
}
