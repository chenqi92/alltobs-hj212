package com.alltobs.hj212.model;

import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.field.C;
import com.alltobs.hj212.validator.field.N;
import com.alltobs.hj212.validator.field.ValidDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * 功能:
 *
 * @author chenQi
 */
@Data
public class CpData {

    public static String LIVESIDE = "LiveSide";
    public static String DEVICE = "Device";
    public static String POLLUTION = "Pollution";

    @ValidDate(format = "yyyyMMddHHmmss")
    @JsonProperty("SystemTime")
    @JsonbProperty("SystemTime")
    private String systemTime;

    @ValidDate(format = "yyyyMMddHHmmssSSS", groups = VersionGroup.V2005.class)
    @JsonProperty("QN")
    @JsonbProperty("QN")
    private String qn;

    @N(integer = 3)
    @JsonProperty("QnRtn")
    @JsonbProperty("QnRtn")
    private String qnRtn;

    @N(integer = 3)
    @JsonProperty("ExeRtn")
    @JsonbProperty("ExeRtn")
    private String exeRtn;

    @Max(value = 9999, groups = VersionGroup.V2005.class)
    @Min(value = 30, groups = VersionGroup.V2017.class)
    @Max(value = 3600, groups = VersionGroup.V2017.class)
    @JsonProperty("RtdInterval")
    @JsonbProperty("RtdInterval")
    private Integer rtdInterval;

    @Max(value = 99, groups = VersionGroup.V2017.class)
    @JsonProperty("MinInterval")
    @JsonbProperty("MinInterval")
    private Integer minInterval;

    @ValidDate(format = "yyyyMMddHHmmss", groups = VersionGroup.V2017.class)
    @JsonProperty("RestartTime")
    @JsonbProperty("RestartTime")
    private String restartTime;

    @ValidDate(format = "yyyyMMddHHmmss", groups = VersionGroup.V2005.class)
    @JsonbProperty("AlarmTime")
    @JsonProperty("AlarmTime")
    private String alarmTime;

    @N(integer = 1, min = 0, max = 1, groups = VersionGroup.V2005.class)
    @JsonbProperty("AlarmType")
    @JsonProperty("AlarmType")
    private String alarmType;

    @N(integer = 20, groups = VersionGroup.V2005.class)
    @JsonbProperty("ReportTarget")
    @JsonProperty("ReportTarget")
    private String reportTarget;

    @C(len = 6)
    @JsonbProperty("PolId")
    @JsonProperty("PolId")
    private String polId;

    @ValidDate(format = "yyyyMMddHHmmss")
    @JsonProperty("BeginTime")
    @JsonbProperty("BeginTime")
    private String beginTime;

    @ValidDate(format = "yyyyMMddHHmmss")
    @JsonProperty("EndTime")
    @JsonbProperty("EndTime")
    private String endTime;

    @ValidDate(format = "yyyyMMddHHmmss")
    @JsonProperty("DataTime")
    @JsonbProperty("DataTime")
    private String dataTime;

    @N(integer = 4, groups = VersionGroup.V2005.class)
    @JsonProperty("ReportTime")
    @JsonbProperty("ReportTime")
    private String reportTime;

    @N(integer = 14, groups = VersionGroup.V2005.class)
    @JsonProperty("DayStdValue")
    @JsonbProperty("DayStdValue")
    private String dayStdValue;

    @N(integer = 14, groups = VersionGroup.V2005.class)
    @JsonProperty("NightStdValue")
    @JsonbProperty("NightStdValue")
    private String nightStdValue;

    @Max(value = 9999, groups = VersionGroup.V2005.class)
    @JsonProperty("PNO")
    @JsonbProperty("PNO")
    private Integer pNo;

    @Max(value = 9999, groups = VersionGroup.V2005.class)
    @JsonProperty("PNUM")
    @JsonbProperty("PNUM")
    private Integer pNum;

    @C(len = 6, groups = VersionGroup.V2005.class)
    @JsonProperty("PW")
    @JsonbProperty("PW")
    private String pw;

    @C(len = 6, groups = VersionGroup.V2017.class)
    @JsonProperty("NewPW")
    @JsonbProperty("NewPW")
    private String newPW;

    @Max(value = 99999, groups = VersionGroup.V2005.class)
    @Max(value = 99, groups = VersionGroup.V2017.class)
    @JsonProperty("OverTime")
    @JsonbProperty("OverTime")
    private Integer overTime;

    @Max(value = 99)
    @JsonProperty("ReCount")
    @JsonbProperty("ReCount")
    private Integer reCount;

    @Max(value = 99999, groups = VersionGroup.V2005.class)
    @JsonProperty("WarnTime")
    @JsonbProperty("WarnTime")
    private Integer warnTime;

    @Max(value = 99, groups = VersionGroup.V2005.class)
    @Min(value = 0, groups = VersionGroup.V2017.class)
    @Max(value = 24, groups = VersionGroup.V2017.class)
    @JsonProperty("CTime")
    @JsonbProperty("CTime")
    @JsonAlias({"Ctime", "cTime"})
    private Integer cTime;

    @Min(value = 0, groups = VersionGroup.V2017.class)
    @Max(value = 99, groups = VersionGroup.V2017.class)
    @JsonProperty("VaseNo")
    @JsonbProperty("VaseNo")
    private Integer vaseNo;

    @ValidDate(format = "HHmmss", groups = VersionGroup.V2017.class)
    @JsonProperty("CstartTime")
    @JsonbProperty("CstartTime")
    private String cStartTime;

    @Min(value = 0, groups = VersionGroup.V2017.class)
    @Max(value = 120, groups = VersionGroup.V2017.class)
    @JsonProperty("Stime")
    @JsonbProperty("Stime")
    private Integer sTime;

    @C(len = 6, groups = VersionGroup.V2017.class)
    @JsonProperty("InfoId")
    @JsonbProperty("InfoId")
    private String infoId;

    @JsonProperty("Flag")
    @JsonbProperty("Flag")
    private List<HjDataFlag> dataFlag;

    @JsonProperty("Pollution")
    @JsonbProperty("Pollution")
    private Map<String, Pollution> pollution;

    @JsonProperty("Device")
    @JsonbProperty("Device")
    private Map<String, Device> device;

    @JsonProperty("LiveSide")
    @JsonbProperty("LiveSide")
    private Map<String, LiveSide> liveSide;
}
