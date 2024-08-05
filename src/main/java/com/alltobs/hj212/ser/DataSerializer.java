package com.alltobs.hj212.ser;

import com.alltobs.hj212.config.SegmentGenerator;
import com.alltobs.hj212.converter.DataReverseConverter;
import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.SegmentFormatException;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.format.T212Generator;
import com.alltobs.hj212.format.VerifyUtil;
import com.alltobs.hj212.model.HjData;
import com.alltobs.hj212.model.verify.PacketElement;
import com.alltobs.hj212.model.verify.T212Map;

import javax.validation.Validator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 功能:
 *
 * @author chenQi
 */
public class DataSerializer
        implements T212Serializer<HjData>, Configured<DataSerializer> {

    private int verifyFeature;
    private Configurator<SegmentGenerator> segmentGeneratorConfigurator;
    private SegmentSerializer<Map<String, Object>> segmentSerializer;
    private Configurator<DataReverseConverter> dataReverseConverterConfigurator;
    private Validator validator;

//    private int version;


    @Override
    public void configured(Configurator<DataSerializer> configurator) {
        configurator.config(this);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void serialize(T212Generator generator, HjData data) throws IOException, T212FormatException {
        generator.writeHeader();

        char[] segment = serialize(data);

        if (VerifyFeature.DATA_LEN_RANGE.enabledIn(verifyFeature)) {
            int segmentLen = segment.length;
            VerifyUtil.verifyRange(segmentLen, 0, 1024, PacketElement.DATA_LEN);
        }
        generator.writeDataAndLenAndCrc(segment);
        generator.writeFooter();
    }

    public char[] serialize(HjData data) throws IOException, T212FormatException {
        StringWriter writer = new StringWriter();
        SegmentGenerator generator = new SegmentGenerator(writer);
        generator.configured(segmentGeneratorConfigurator);

        Map<String, Object> map = convert(data);
        try {
            segmentSerializer.serialize(generator, map);
        } catch (SegmentFormatException e) {
            T212FormatException.segment_exception(e);
        }
        return writer.toString().toCharArray();
    }

    public T212Map<String, Object> convert(HjData data) throws T212FormatException {
        DataReverseConverter dataConverter = new DataReverseConverter();
        dataConverter.configured(dataReverseConverterConfigurator);
        T212Map<String, Object> map = dataConverter.convert(data);

        return map;
    }

    public void setVerifyFeature(int verifyFeature) {
        this.verifyFeature = verifyFeature;
    }

    public void setSegmentGeneratorConfigurator(Configurator<SegmentGenerator> segmentGeneratorConfigurator) {
        this.segmentGeneratorConfigurator = segmentGeneratorConfigurator;
    }

    public void setSegmentSerializer(SegmentSerializer<Map<String, Object>> segmentSerializer) {
        this.segmentSerializer = segmentSerializer;
    }

    public void setDataReverseConverterConfigurator(Configurator<DataReverseConverter> dataReverseConverterConfigurator) {
        this.dataReverseConverterConfigurator = dataReverseConverterConfigurator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
