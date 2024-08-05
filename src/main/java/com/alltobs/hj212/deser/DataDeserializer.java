package com.alltobs.hj212.deser;

import com.alltobs.hj212.config.SegmentParser;
import com.alltobs.hj212.converter.DataConverter;
import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.exception.SegmentFormatException;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.format.T212Parser;
import com.alltobs.hj212.format.VerifyUtil;
import com.alltobs.hj212.model.HjData;
import com.alltobs.hj212.model.verify.PacketElement;
import com.alltobs.hj212.model.verify.T212Map;
import com.alltobs.hj212.model.verify.groups.ModeGroup;
import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.clazz.FieldValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象 级别 反序列化器
 *
 * @author ChenQi
 */
public class DataDeserializer
        implements T212Deserializer<HjData>, Configured<DataDeserializer> {

    private int verifyFeature;
    private Configurator<SegmentParser> segmentParserConfigurator;
    private SegmentDeserializer<Map<String, Object>> segmentDeserializer;
    private Configurator<DataConverter> dataConverterConfigurator;
    private Validator validator;


    @Override
    public void configured(Configurator<DataDeserializer> configurator) {
        configurator.config(this);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public HjData deserialize(T212Parser parser) throws IOException, T212FormatException {
        parser.readHeader();
        int len = parser.readInt32(10);
        if (len == -1) {
            T212FormatException.length_not_range(PacketElement.DATA_LEN, len, 4, 4);
        }
        if (VerifyFeature.DATA_LEN_RANGE.enabledIn(verifyFeature)) {
            VerifyUtil.verifyRange(len, 0, 1024, PacketElement.DATA_LEN);
        }
        char[] data = parser.readData(len);
        int crc = parser.readInt32(16);

        if (VerifyFeature.DATA_CRC.enabledIn(verifyFeature)) {
            if (crc == -1 ||
                    T212Parser.crc16Checkout(data, len) != crc) {
                T212FormatException.crc_verification_failed(PacketElement.DATA, data, crc);
            }
        }
//        parser.readFooter();
        return deserialize(data);
    }

    public HjData deserialize(char[] data) throws IOException, T212FormatException {
        PushbackReader reader = new PushbackReader(new CharArrayReader(data));
        SegmentParser parser = new SegmentParser(reader);
        parser.configured(segmentParserConfigurator);

        Map<String, Object> result = null;
        try {
            result = segmentDeserializer.deserialize(parser);
        } catch (SegmentFormatException e) {
            T212FormatException.segment_exception(e);
        }
        return deserialize(result);
    }

    public HjData deserialize(Map<String, Object> map) throws T212FormatException {
        DataConverter dataConverter = new DataConverter();
        dataConverter.configured(dataConverterConfigurator);
        HjData result = dataConverter.convert(T212Map.createCpDataLevel(map));

        if (VerifyFeature.USE_VERIFICATION.enabledIn(verifyFeature)) {
            verify(result);
        }
        return result;
    }

    private void verify(HjData result) throws T212FormatException {
        List<Class> groups = new ArrayList<>();
        groups.add(Default.class);
        if (HjDataFlag.V0.isMarked(result.getDataFlag())) {
            groups.add(VersionGroup.V2017.class);
        } else {
            groups.add(VersionGroup.V2005.class);
        }
        if (HjDataFlag.D.isMarked(result.getDataFlag())) {
            groups.add(ModeGroup.UseSubPacket.class);
        }

        Set<ConstraintViolation<HjData>> constraintViolationSet =
                validator.validate(result, groups.toArray(new Class[]{}));
        if (!constraintViolationSet.isEmpty()) {
            if (VerifyFeature.THROW_ERROR_VERIFICATION_FAILED.enabledIn(verifyFeature)) {
                FieldValidator.create_format_exception(constraintViolationSet, result);
            } else {
                //TODO set context
            }
        }
    }

    public void setVerifyFeature(int verifyFeature) {
        this.verifyFeature = verifyFeature;
    }

    public void setSegmentParserConfigurator(Configurator<SegmentParser> segmentParserConfigurator) {
        this.segmentParserConfigurator = segmentParserConfigurator;
    }

    public void setSegmentDeserializer(SegmentDeserializer<Map<String, Object>> segmentDeserializer) {
        this.segmentDeserializer = segmentDeserializer;
    }

    public void setDataConverterConfigurator(Configurator<DataConverter> mapperConfigurator) {
        this.dataConverterConfigurator = mapperConfigurator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
