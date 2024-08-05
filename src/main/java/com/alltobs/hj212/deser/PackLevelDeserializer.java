package com.alltobs.hj212.deser;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.VerifyFeature;
import com.alltobs.hj212.format.T212Parser;
import com.alltobs.hj212.format.VerifyUtil;
import com.alltobs.hj212.model.Pack;
import com.alltobs.hj212.model.verify.PacketElement;

import java.io.IOException;

/**
 * 功能: 通信包 级别 反序列化器
 *
 * @author chenQi
 */
public class PackLevelDeserializer
        implements T212Deserializer<Pack>, Configured<PackLevelDeserializer> {

    private Configurator<T212Parser> parserConfigurator;
    private int verifyFeature;

    @Override
    public void configured(Configurator<PackLevelDeserializer> configurator) {
        configurator.config(this);
    }

    @Override
    public Pack deserialize(T212Parser parser) throws IOException, T212FormatException {
        parser.configured(parserConfigurator);

        Pack pack = new Pack();
        pack.setHeader(parser.readHeader());
        pack.setLength(parser.readDataLen());

        int segmentLen = Integer.parseInt(new String(pack.getLength()));
        if (VerifyFeature.DATA_LEN_RANGE.enabledIn(verifyFeature)) {
            VerifyUtil.verifyRange(segmentLen, 0, 1024, PacketElement.DATA_LEN);
        }
        pack.setData(parser.readData(segmentLen));
        pack.setCrc(parser.readCrc());

        if (VerifyFeature.DATA_CRC.enabledIn(verifyFeature)) {
            VerifyUtil.verifyCrc(pack.getSegment(), pack.getCrc(), PacketElement.DATA_CRC);
        }
        pack.setFooter(parser.readFooter());

        return pack;
    }

    public void setVerifyFeature(int verifyFeature) {
        this.verifyFeature = verifyFeature;
    }

    public void setParserConfigurator(Configurator<T212Parser> parserConfigurator) {
        this.parserConfigurator = parserConfigurator;
    }

}
