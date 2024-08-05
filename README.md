## 依赖jar包
| 引入包             | 版本    |
| ------------------ | ------- |
| jdk                | 1.8     |
| spring boot        | 2.7.2   |
| allbs-common | 1.1.6 |
| jackson-datatype-jsr310 | 2.13.3 |
| jackson-databind | 2.13.3 |
| spring-boot-starter-validation | 2.7.2 |
| javax.json.bind-api | 1.0 |

## 使用
### 添加依赖
{% tabs tag-hide %}
<!-- tab maven -->
```xml
<dependency>
  <groupId>com.alltobs</groupId>
  <artifactId>allbs-hj212</artifactId>
  <version>1.1.8</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    <version>2.7.2</version>
</dependency>
```

<!-- endtab -->

<!-- tab Gradle -->

```
implementation 'com.alltobs:allbs-hj212:1.1.8'
```

<!-- endtab -->

<!-- tab Kotlin -->

```
implementation("com.alltobs:allbs-hj212:1.1.8")
```
<!-- endtab -->
{% endtabs %}

### 数据包中CP不转换

```java
String h212 = "##0435QN=20210301111100112;ST=21;CN=2011;PW=123456;MN=ZLDSZ20210127;Flag=8;CP=&&DataTime=20210301111100;w01001-Rtd=8.33,w01001-Flag=N;w01009-Rtd=10.15,w01009-Flag=N;w01010-Rtd=9.7,w01010-Flag=N;w01014-Rtd=1425,w01014-Flag=N;w01003-Rtd=13.10,w01003-Flag=N;w21011-Rtd=0,w21011-Flag=N;w21001-Rtd=3.563168,w21001-Flag=N;w21003-Rtd=0.09055002,w21003-Flag=N;w01019-Rtd=4.102818,w01019-Flag=N;w01008-Rtd=0,w01008-Flag=N;w23002-Rtd=0,w23002-Flag=N&&7d00\r\n";
T212Mapper mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();
Map<String, Object> resultMap = new HashMap<>(2);
mapper.readMap(h212)
```

#### 结果

```json
{
  "QN": "20210301111100112",
  "ST": "21",
  "CN": "2011",
  "PW": "123456",
  "MN": "ZLDSZ20210127",
  "Flag": "8",
  "CP": "DataTime=20210301111100;w01001-Rtd=8.33,w01001-Flag=N;w01009-Rtd=10.15,w01009-Flag=N;w01010-Rtd=9.7,w01010-Flag=N;w01014-Rtd=1425,w01014-Flag=N;w01003-Rtd=13.10,w01003-Flag=N;w21011-Rtd=0,w21011-Flag=N;w21001-Rtd=3.563168,w21001-Flag=N;w21003-Rtd=0.09055002,w21003-Flag=N;w01019-Rtd=4.102818,w01019-Flag=N;w01008-Rtd=0,w01008-Flag=N;w23002-Rtd=0,w23002-Flag=N"
}
```

### 完全转换map
```java
String h212 = "##0435QN=20210301111100112;ST=21;CN=2011;PW=123456;MN=ZLDSZ20210127;Flag=8;CP=&&DataTime=20210301111100;w01001-Rtd=8.33,w01001-Flag=N;w01009-Rtd=10.15,w01009-Flag=N;w01010-Rtd=9.7,w01010-Flag=N;w01014-Rtd=1425,w01014-Flag=N;w01003-Rtd=13.10,w01003-Flag=N;w21011-Rtd=0,w21011-Flag=N;w21001-Rtd=3.563168,w21001-Flag=N;w21003-Rtd=0.09055002,w21003-Flag=N;w01019-Rtd=4.102818,w01019-Flag=N;w01008-Rtd=0,w01008-Flag=N;w23002-Rtd=0,w23002-Flag=N&&7d00\r\n";
T212Mapper mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();
Map<String, Object> resultMap = new HashMap<>(2);
mapper.readDeepMap(h212)
```

#### 结果
```json
{
  "QN": "20210301111100112",
  "ST": "21",
  "CN": "2011",
  "PW": "123456",
  "MN": "ZLDSZ20210127",
  "Flag": "8",
  "CP": {
    "DataTime": "20210301111100",
    "w01001-Rtd": "8.33",
    "w01001-Flag": "N",
    "w01009-Rtd": "10.15",
    "w01009-Flag": "N",
    "w01010-Rtd": "9.7",
    "w01010-Flag": "N",
    "w01014-Rtd": "1425",
    "w01014-Flag": "N",
    "w01003-Rtd": "13.10",
    "w01003-Flag": "N",
    "w21011-Rtd": "0",
    "w21011-Flag": "N",
    "w21001-Rtd": "3.563168",
    "w21001-Flag": "N",
    "w21003-Rtd": "0.09055002",
    "w21003-Flag": "N",
    "w01019-Rtd": "4.102818",
    "w01019-Flag": "N",
    "w01008-Rtd": "0",
    "w01008-Flag": "N",
    "w23002-Rtd": "0",
    "w23002-Flag": "N"
  }
}
```

### 转换为实体类

```java
String h212 = "##0435QN=20210301111100112;ST=21;CN=2011;PW=123456;MN=ZLDSZ20210127;Flag=8;CP=&&DataTime=20210301111100;w01001-Rtd=8.33,w01001-Flag=N;w01009-Rtd=10.15,w01009-Flag=N;w01010-Rtd=9.7,w01010-Flag=N;w01014-Rtd=1425,w01014-Flag=N;w01003-Rtd=13.10,w01003-Flag=N;w21011-Rtd=0,w21011-Flag=N;w21001-Rtd=3.563168,w21001-Flag=N;w21003-Rtd=0.09055002,w21003-Flag=N;w01019-Rtd=4.102818,w01019-Flag=N;w01008-Rtd=0,w01008-Flag=N;w23002-Rtd=0,w23002-Flag=N&&7d00\r\n";
T212Mapper mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();
Map<String, Object> resultMap = new HashMap<>(2);
mapper.readData(h212)
```

#### 结果
```json
{
  "QN": "20210301111100112",
  "PNUM": null,
  "PNO": null,
  "ST": "21",
  "CN": "2011",
  "PW": "123456",
  "MN": "ZLDSZ20210127",
  "Flag": [
    "V1"
  ],
  "CP": {
    "SystemTime": null,
    "QN": null,
    "QnRtn": null,
    "ExeRtn": null,
    "RtdInterval": null,
    "MinInterval": null,
    "RestartTime": null,
    "AlarmTime": null,
    "AlarmType": null,
    "ReportTarget": null,
    "PolId": null,
    "BeginTime": null,
    "EndTime": null,
    "DataTime": "20210301111100",
    "ReportTime": null,
    "DayStdValue": null,
    "NightStdValue": null,
    "PNO": null,
    "PNUM": null,
    "PW": null,
    "NewPW": null,
    "OverTime": null,
    "ReCount": null,
    "WarnTime": null,
    "CTime": null,
    "VaseNo": null,
    "CstartTime": null,
    "Stime": null,
    "InfoId": null,
    "Flag": null,
    "Pollution": {
      "w21011": {
        "SampleTime": null,
        "Rtd": 0,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w21001": {
        "SampleTime": null,
        "Rtd": 3.563168,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w23002": {
        "SampleTime": null,
        "Rtd": 0,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w21003": {
        "SampleTime": null,
        "Rtd": 0.09055002,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01001": {
        "SampleTime": null,
        "Rtd": 8.33,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01003": {
        "SampleTime": null,
        "Rtd": 13.1,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01014": {
        "SampleTime": null,
        "Rtd": 1425,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01010": {
        "SampleTime": null,
        "Rtd": 9.7,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01008": {
        "SampleTime": null,
        "Rtd": 0,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01019": {
        "SampleTime": null,
        "Rtd": 4.102818,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      },
      "w01009": {
        "SampleTime": null,
        "Rtd": 10.15,
        "Min": null,
        "Avg": null,
        "Max": null,
        "ZsRtd": null,
        "ZsMin": null,
        "ZsAvg": null,
        "ZsMax": null,
        "Flag": "N",
        "EFlag": null,
        "Cou": null,
        "RS": null,
        "RT": null,
        "Ala": null,
        "UpValue": null,
        "LowValue": null,
        "Data": null,
        "DayData": null,
        "NightData": null
      }
    },
    "Device": null,
    "LiveSide": null
  }
}
```

### 生成Hj212编码

#### 先构建Cp再生成hj212
```java
HjData data = new HjData();
data.setSt("32");
data.setCn("2011");
data.setPw("123456");
data.setMn("NJGDKYYC202101q0001w0001");

CpData cp = new CpData();
data.setCp(cp);
cp.setDataTime("20210305003817000");

Map<String, Pollution> pollutionMap = new LinkedHashMap<>();
cp.setPollution(pollutionMap);

Pollution ele01 = new Pollution();
pollutionMap.put("ele01", ele01);
ele01.setRtd(new BigDecimal("1"));

Pollution ele02 = new Pollution();
pollutionMap.put("ele02", ele02);
ele02.setRtd(new BigDecimal("0"));
T212Mapper mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();
String result = "";
try {
    result = mapper.writeDataAsString(data);
} catch (Exception e) {
    log.error("转换失败" + e.getLocalizedMessage());
}
```

### 已有Cp生成hj212编码
```java
String data = "ST=27;Flag=4;CN=2011;PW=123456;MN=DYGLO000001A000001;CP=&&DataTime=20210823075400;a01011-Rtd=0.00,a01011-Flag=N;a01014-Rtd=15.00,a01014-Flag=N;a01015-Rtd=41.10,a01015-Flag=N;a01013-Rtd=-18.74,a01013-Flag=N;a00000-Rtd=0.00,a00000-Flag=N;a05002-Rtd=-0.94,a05002-Flag=N;a24088-Rtd=1.25,a24088-Flag=N;a05002-Cou=0.00,a05002-Flag=N;a24088-Cou=0.00,a24088-Flag=N;&&";
StringWriter writer = new StringWriter();
T212Generator generator = new T212Generator(writer);
generator.setGeneratorFeature(Feature.collectFeatureDefaults(GeneratorFeature.class));
try {
    generator.writeHeader();
    generator.writeDataAndLenAndCrc(data.toCharArray());
    generator.writeFooter();
    System.out.println(writer.toString());
} catch (Exception e) {
    log.error("转换失败" + e.getLocalizedMessage());
}
```

#### 结果
```
##0109ST=32;CN=2011;PW=123456;MN=NJGDKYYC202101q0001w0001;CP=&&DataTime=20210305003817000;ele01-Rtd=1;ele02-Rtd=0&&8c41\r\n
```
