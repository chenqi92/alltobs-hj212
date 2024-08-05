package com.alltobs.hj212.model;

import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.field.C;
import com.alltobs.hj212.validator.field.N;
import com.alltobs.hj212.validator.field.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * 功能:污染因子
 *
 * @author chenQi
 */
@Data
public class Pollution {

    @Schema(title = "污染物采样时间", name = "SampleTime")
    @ValidDate(format = "yyyyMMddHHmmssSSS", groups = VersionGroup.V2017.class)
    @JsonProperty("SampleTime")
    @JsonbProperty("SampleTime")
    private String sampleTime;

    @Schema(title = "污染物实时采样数据", name = "Rtd")
    @JsonProperty("Rtd")
    @JsonbProperty("Rtd")
    private BigDecimal rtd;

    @Schema(title = "污染物指定时间内最小值", name = "Min")
    @JsonProperty("Min")
    @JsonbProperty("Min")
    private BigDecimal min;

    @Schema(title = "污染物指定时间内平均值", name = "Avg")
    @JsonProperty("Avg")
    @JsonbProperty("Avg")
    private BigDecimal avg;

    @Schema(title = "污染物指定时间内最大值", name = "Max")
    @JsonProperty("Max")
    @JsonbProperty("Max")
    private BigDecimal max;

    @Schema(title = "污染物实时采样折算数据", name = "ZsRtd")
    @JsonProperty("ZsRtd")
    @JsonbProperty("ZsRtd")
    private BigDecimal zsRtd;

    @Schema(title = "污染物指定时间内最小折算值", name = "ZsMin")
    @JsonProperty("ZsMin")
    @JsonbProperty("ZsMin")
    private BigDecimal zsMin;

    @Schema(title = "污染物指定时间内平均折算值", name = "ZsAvg")
    @JsonProperty("ZsAvg")
    @JsonbProperty("ZsAvg")
    private BigDecimal zsAvg;

    @Schema(title = "污染物指定时间内最大折算值", name = "ZsMax")
    @JsonProperty("ZsMax")
    @JsonbProperty("ZsMax")
    private BigDecimal zsMax;

    @Schema(title = "监测污染物实时数据标记", name = "Flag", allowableValues = "可扩充[P,F,C,M,T,D,S,N,0,1,2,3]")
    @C(len = 1, groups = VersionGroup.V2017.class)
    @JsonProperty("Flag")
    @JsonbProperty("Flag")
    private String flag;

    @Schema(title = "监测仪器扩充数据标记", name = "EFlag")
    @C(len = 4, groups = VersionGroup.V2017.class)
    @JsonProperty("EFlag")
    @JsonbProperty("EFlag")
    private String eFlag;

    @Schema(title = "污染物指定时间内累计值", name = "Cou")
    @JsonProperty("Cou")
    @JsonbProperty("Cou")
    private BigDecimal cou;


    @Schema(title = "设备运行状态的实时采样值", name = "RS")
    @Max(value = 1, groups = VersionGroup.V2005.class)
//    @N(integer = 1, groups = VersionGroup.V2005.class)
    @JsonProperty("RS")
    @JsonbProperty("RS")
    private Integer rs;

    @Schema(title = "设备指定时间内的运行时间", name = "RT")
    @DecimalMax(value = "24", groups = VersionGroup.V2005.class)
//    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @JsonProperty("RT")
    @JsonbProperty("RT")
    private BigDecimal rt;

    @Schema(title = "污染物报警期间内采样值", name = "Ala")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @JsonProperty("Ala")
    @JsonbProperty("Ala")
    private BigDecimal ala;

    @Schema(title = "污染物报警上限值", name = "UpValue")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @JsonProperty("UpValue")
    @JsonbProperty("UpValue")
    private BigDecimal upValue;

    @Schema(title = "污染物报警下限值", name = "LowValue")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @JsonProperty("LowValue")
    @JsonbProperty("LowValue")
    private BigDecimal lowValue;

    @Schema(title = "噪声监测日历史数据", name = "Data")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @N(integer = 3, fraction = 1, groups = VersionGroup.V2017.class)
    @JsonProperty("Data")
    @JsonbProperty("Data")
    private String data;

    @Schema(title = "噪声昼间历史数据", name = "DayData")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @N(integer = 3, fraction = 1, groups = VersionGroup.V2017.class)
    @JsonProperty("DayData")
    @JsonbProperty("DayData")
    private String dayData;

    @Schema(title = "噪声夜间历史数据", name = "NightData")
    @N(integer = 14, fraction = 2, groups = VersionGroup.V2005.class)
    @N(integer = 3, fraction = 1, groups = VersionGroup.V2017.class)
    @JsonProperty("NightData")
    @JsonbProperty("NightData")
    private String nightData;
}
