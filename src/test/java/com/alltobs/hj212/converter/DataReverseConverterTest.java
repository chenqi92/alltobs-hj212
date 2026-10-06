package com.alltobs.hj212.converter;

import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.CpData;
import com.alltobs.hj212.model.HjData;
import com.alltobs.hj212.model.verify.T212Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DataReverseConverterTest {

    static Stream<Arguments> flags() {
        return Stream.of(
                Arguments.of(List.of(HjDataFlag.A), "1"),
                Arguments.of(List.of(HjDataFlag.D), "2"),
                Arguments.of(List.of(HjDataFlag.V0), "4"),
                Arguments.of(List.of(HjDataFlag.V1), "8"),
                Arguments.of(List.of(HjDataFlag.V2), "16"),
                Arguments.of(List.of(HjDataFlag.V3), "32"),
                Arguments.of(List.of(HjDataFlag.V4), "64"),
                Arguments.of(List.of(HjDataFlag.V5), "128"),
                Arguments.of(List.of(HjDataFlag.A, HjDataFlag.D, HjDataFlag.V0), "7"),
                Arguments.of(List.of(HjDataFlag.A, HjDataFlag.A, HjDataFlag.V1), "9"),
                Arguments.of(Arrays.asList(HjDataFlag.values()), "255")
        );
    }

    @ParameterizedTest
    @MethodSource("flags")
    void encodesFlagsInBothDataLevels(List<HjDataFlag> flags, String expected) {
        HjData data = new HjData();
        data.setDataFlag(flags);
        CpData cp = new CpData();
        cp.setDataFlag(flags);
        data.setCp(cp);

        T212Map<String, Object> result = converter().convert(data);

        assertEquals(expected, result.get("Flag"));
        assertEquals(expected, ((Map<?, ?>) result.get("CP")).get("Flag"));
    }

    @Test
    void preservesFlagsWhenConvertedBackToData() {
        HjData data = new HjData();
        List<HjDataFlag> flags = List.of(HjDataFlag.A, HjDataFlag.D, HjDataFlag.V0);
        data.setDataFlag(flags);
        CpData cp = new CpData();
        cp.setDataFlag(List.of(HjDataFlag.A, HjDataFlag.V1));
        data.setCp(cp);
        DataConverter decoder = new DataConverter();
        decoder.setObjectMapper(mapper());

        HjData result = decoder.convert(converter().convert(data));

        assertEquals(flags, result.getDataFlag());
        assertEquals(cp.getDataFlag(), result.getCp().getDataFlag());
    }

    @Test
    void omitsAbsentFlags() {
        HjData data = new HjData();
        data.setCp(new CpData());

        T212Map<String, Object> result = converter().convert(data);

        assertFalse(result.containsKey("Flag"));
        assertFalse(((Map<?, ?>) result.get("CP")).containsKey("Flag"));
    }

    private DataReverseConverter converter() {
        DataReverseConverter converter = new DataReverseConverter();
        converter.setObjectMapper(mapper());
        return converter;
    }

    private ObjectMapper mapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
