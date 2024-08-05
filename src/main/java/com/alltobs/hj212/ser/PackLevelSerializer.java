package com.alltobs.hj212.ser;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.format.T212Generator;
import com.alltobs.hj212.format.VerifyUtil;
import com.alltobs.hj212.model.Pack;
import com.alltobs.hj212.model.verify.PacketElement;

import java.io.IOException;
import java.util.Arrays;

/**
 * 功能:
 *
 * @author chenQi
 */
public class PackLevelSerializer
        implements T212Serializer<Pack>, Configured<PackLevelSerializer> {

    private Configurator<T212Generator> generatorConfigurator;
    private int verifyFeature;

    @Override
    public void configured(Configurator<PackLevelSerializer> configurator) {
        configurator.config(this);
    }

    @Override
    public void serialize(T212Generator generator, Pack pack) throws IOException, T212FormatException {
        generator.configured(generatorConfigurator);

        generator.writeHeader();

        if (VerifyFeature.DATA_LEN_RANGE.enabledIn(verifyFeature)) {
            int segmentLen = 0;
            if (!Arrays.equals(pack.getLength(), new char[]{0, 0, 0, 0})) {
                segmentLen = Integer.parseInt(new String(pack.getLength()));
            }
            if (segmentLen == 0) {
                segmentLen = pack.getSegment().length;
            }

            VerifyUtil.verifyRange(segmentLen, 0, 1024, PacketElement.DATA_LEN);
        }

        generator.writeDataAndLenAndCrc(pack.getSegment());

        if (VerifyFeature.DATA_CRC.enabledIn(verifyFeature)) {
            if (Arrays.equals(pack.getCrc(), new char[]{0, 0, 0, 0})) {
                //ignore
            } else {
                VerifyUtil.verifyCrc(pack.getSegment(), pack.getCrc(), PacketElement.DATA_CRC);
            }
        }

        generator.writeFooter();
    }

    public void setVerifyFeature(int verifyFeature) {
        this.verifyFeature = verifyFeature;
    }

    public void setGeneratorConfigurator(Configurator<T212Generator> generatorConfigurator) {
        this.generatorConfigurator = generatorConfigurator;
    }

}
