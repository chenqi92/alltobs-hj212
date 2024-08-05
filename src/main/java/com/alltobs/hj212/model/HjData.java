package com.alltobs.hj212.model;

import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.verify.groups.ModeGroup;
import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.field.C;
import com.alltobs.hj212.validator.field.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 功能:
 *
 * @author chenQi
 */
@Data
public class HjData {

    public static String CP = "CP";
    public static String FLAG = "Flag";


    @Schema(title = "请求编号", name = "QN")
    @ValidDate(format = "yyyyMMddHHmmssSSS")
    @JsonProperty("QN")
    @JsonbProperty("QN")
    private String qn;

    @Schema(title = "总包号", name = "PNUM")
    @Max(value = 9999)
    @Min(value = 1, groups = ModeGroup.UseSubPacket.class)
    @JsonProperty("PNUM")
    @JsonbProperty("PNUM")
    private Integer pNum;

    @Schema(title = "包号", name = "PNO")
    @Max(value = 9999)
    @Min(value = 1, groups = ModeGroup.UseSubPacket.class)
    @JsonProperty("PNO")
    @JsonbProperty("PNO")
    private Integer pNo;

    @Schema(title = "系统编号", name = "ST")
    @C(len = 2)
    @JsonProperty("ST")
    @JsonbProperty("ST")
    private String st;

    @Schema(title = "命令编号", name = "CN")
    @C(len = 4)
    @JsonProperty("CN")
    @JsonbProperty("CN")
    private String cn;

    @Schema(title = "访问密码", name = "PW")
    @C(len = 6)
    @JsonProperty("PW")
    @JsonbProperty("PW")
    private String pw;

    @Schema(title = "设备唯一标识", name = "MN")
    @C(len = 14, groups = VersionGroup.V2005.class)
    @C(len = 24, groups = VersionGroup.V2017.class)
    @JsonProperty("MN")
    @JsonbProperty("MN")
    private String mn;

    @Schema(title = "是否拆分包及应答标志", name = "Flag")
    @JsonProperty("Flag")
    @JsonbProperty("Flag")
    private List<HjDataFlag> dataFlag;

    @Schema(title = "指令参数", name = "CP")
    @Valid
    @JsonProperty("CP")
    @JsonbProperty("CP")
    private CpData cp;
}
