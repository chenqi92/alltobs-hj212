package com.alltobs.hj212.format;

import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.model.CpData;
import com.alltobs.hj212.model.HjData;
import com.alltobs.hj212.model.Pollution;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class T212MapperTest {

    @Test
    void roundTripsFlagsAndPollutionValues() throws Exception {
        HjData data = data();
        T212Mapper mapper = mapper();

        HjData result = mapper.readData(mapper.writeDataAsString(data));

        assertEquals(data.getDataFlag(), result.getDataFlag());
        assertEquals(data.getMn(), result.getMn());
        assertEquals(data.getCp().getDataTime(), result.getCp().getDataTime());
        assertEquals(data.getCp().getDataFlag(), result.getCp().getDataFlag());
        assertEquals(new BigDecimal("8.330"), result.getCp().getPollution().get("w01001").getRtd());
    }

    @Test
    void readsRawAndDeepMaps() throws Exception {
        T212Mapper mapper = mapper();
        String packet = mapper.writeDataAsString(data());

        Map<String, String> raw = mapper.readMap(packet);
        assertEquals("5", raw.get("Flag"));
        assertTrue(raw.get("CP").contains("w01001-Rtd=8.330"));
        Map<String, Object> deep = mapper.readDeepMap(packet);
        Map<?, ?> cp = (Map<?, ?>) deep.get("CP");
        assertEquals("20210305003817", cp.get("DataTime"));
        assertEquals("9", cp.get("Flag"));
        assertEquals("8.330", cp.get("w01001-Rtd"));
    }

    @Test
    void readsDataFromFragmentedReader() throws Exception {
        T212Mapper mapper = mapper();
        StringReader reader = new StringReader(mapper.writeDataAsString(data())) {
            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                return super.read(buffer, offset, Math.min(length, 1));
            }
        };

        HjData result = mapper.readData(reader);

        assertEquals(List.of(HjDataFlag.A, HjDataFlag.V0), result.getDataFlag());
        assertEquals(new BigDecimal("8.330"), result.getCp().getPollution().get("w01001").getRtd());
    }

    @Test
    void rejectsInvalidCrcWhenVerificationIsEnabled() throws Exception {
        T212Mapper mapper = mapper();
        String packet = mapper.writeDataAsString(data());
        int crcOffset = packet.length() - 6;
        char changedDigit = packet.charAt(crcOffset) == '0' ? '1' : '0';
        String invalid = packet.substring(0, crcOffset) + changedDigit + packet.substring(crcOffset + 1);

        assertThrows(T212FormatException.class, () -> mapper.readData(invalid));
    }

    @Test
    void rejectsTruncatedPacket() {
        assertThrows(T212FormatException.class, () -> mapper().readData("##0005abc"));
    }

    private T212Mapper mapper() {
        return new T212Mapper().enableDefaultParserFeatures().enable(VerifyFeature.DATA_CRC);
    }

    private HjData data() {
        HjData data = new HjData();
        data.setQn("20210305003817000");
        data.setSt("32");
        data.setCn("2011");
        data.setPw("123456");
        data.setMn("123456789012345678901234");
        data.setDataFlag(List.of(HjDataFlag.A, HjDataFlag.V0));
        CpData cp = new CpData();
        cp.setDataTime("20210305003817");
        cp.setDataFlag(List.of(HjDataFlag.A, HjDataFlag.V1));
        Pollution pollution = new Pollution();
        pollution.setRtd(new BigDecimal("8.330"));
        cp.setPollution(Map.of("w01001", pollution));
        data.setCp(cp);
        return data;
    }
}
