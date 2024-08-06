package com.alltobs.hj212.ser;

import com.alltobs.hj212.config.SegmentGenerator;
import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.SegmentFormatException;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.format.T212Generator;
import com.alltobs.hj212.format.VerifyUtil;
import com.alltobs.hj212.model.verify.PacketElement;

import jakarta.validation.Validator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 功能:
 *
 * @author chenQi
 */
public class CpDataLevelMapDataSerializer
        implements T212Serializer<Map<String, Object>>, Configured<CpDataLevelMapDataSerializer> {

    private int verifyFeature;
    private Configurator<SegmentGenerator> segmentGeneratorConfigurator;
    private SegmentSerializer<Map<String, Object>> segmentSerializer;
    private Validator validator;

    @Override
    public void configured(Configurator<CpDataLevelMapDataSerializer> configurator) {
        configurator.config(this);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void serialize(T212Generator generator, Map<String, Object> data) throws IOException, T212FormatException {
        generator.writeHeader();

        char[] segment = serialize(data);

        if (VerifyFeature.DATA_LEN_RANGE.enabledIn(verifyFeature)) {
            int segmentLen = segment.length;
            VerifyUtil.verifyRange(segmentLen, 0, 1024, PacketElement.DATA_LEN);
        }
        generator.writeDataAndLenAndCrc(segment);
        generator.writeFooter();
    }

    public char[] serialize(Map<String, Object> data) throws IOException, T212FormatException {
        StringWriter writer = new StringWriter();
        SegmentGenerator generator = new SegmentGenerator(writer);
        generator.configured(segmentGeneratorConfigurator);

        try {
            segmentSerializer.serialize(generator, data);
        } catch (SegmentFormatException e) {
            T212FormatException.segment_exception(e);
        }
        return writer.toString().toCharArray();
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

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
