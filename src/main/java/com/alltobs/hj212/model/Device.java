package com.alltobs.hj212.model;

import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * 功能:
 *
 * @author chenQi
 */
@Data
public class Device {

    @Schema(title = "污染治理设施运行状态的实时采样值", name = "RS")
    @Max(value = 5, groups = VersionGroup.V2017.class)
    @JsonProperty("RS")
    @JsonbProperty("RS")
    private int rs;

    @Schema(title = "污染治理设施一日内的运行时间", name = "RT")
    @DecimalMax(value = "24", groups = VersionGroup.V2017.class)
    @JsonProperty("RT")
    @JsonbProperty("RT")
    private BigDecimal rt;
}
