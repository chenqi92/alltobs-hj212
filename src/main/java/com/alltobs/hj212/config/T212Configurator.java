package com.alltobs.hj212.config;

import com.alltobs.hj212.converter.DataConverter;
import com.alltobs.hj212.converter.DataReverseConverter;
import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.MultipleConfiguratorAdapter;
import com.alltobs.hj212.deser.*;
import com.alltobs.hj212.format.T212Generator;
import com.alltobs.hj212.format.T212Parser;
import com.alltobs.hj212.ser.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validator;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 功能:
 *
 * @author chenQi
 */
public class T212Configurator extends MultipleConfiguratorAdapter {

    private int segmentParserFeature;
    private int parserFeature;

    private int verifyFeature;
    private Validator validator;
    private ObjectMapper objectMapper;

    private int segmentGeneratorFeature;
    private final int generatorFeature = 0;

    public void setSegmentParserFeature(int segmentParserFeature) {
        this.segmentParserFeature = segmentParserFeature;
    }

    public void setParserFeature(int parserFeature) {
        this.parserFeature = parserFeature;
    }

    public void setVerifyFeature(int verifyFeature) {
        this.verifyFeature = verifyFeature;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setSegmentGeneratorFeature(int segmentGeneratorFeature) {
        this.segmentGeneratorFeature = segmentGeneratorFeature;
    }


    class SegmentParserConfigurator implements Configurator<SegmentParser> {
        @Override
        public void config(SegmentParser parser) {
            T212Configurator.this.configure(parser);
        }
    }

    class T212ParserConfigurator implements Configurator<T212Parser> {
        @Override
        public void config(T212Parser parser) {
            T212Configurator.this.configure(parser);
        }
    }

    class PackLevelDeserializerConfigurator implements Configurator<PackLevelDeserializer> {
        @Override
        public void config(PackLevelDeserializer deserializer) {
            T212Configurator.this.configure(deserializer);
        }
    }

    class DataLevelMapDeserializerConfigurator implements Configurator<DataLevelMapDeserializer> {
        @Override
        public void config(DataLevelMapDeserializer deserializer) {
            T212Configurator.this.configure(deserializer);
        }
    }

    class CpDataLevelMapDeserializerConfigurator implements Configurator<CpDataLevelMapDeserializer> {
        @Override
        public void config(CpDataLevelMapDeserializer deserializer) {
            T212Configurator.this.configure(deserializer);
        }
    }

    class DataDeserializerConfigurator implements Configurator<DataDeserializer> {
        @Override
        public void config(DataDeserializer deserializer) {
            T212Configurator.this.configure(deserializer);
        }
    }

    class DataConverterConfigurator implements Configurator<DataConverter> {
        @Override
        public void config(DataConverter converter) {
            T212Configurator.this.configure(converter);
        }
    }

    class SegmentGeneratorConfigurator implements Configurator<SegmentGenerator> {
        @Override
        public void config(SegmentGenerator generator) {
            T212Configurator.this.configure(generator);
        }
    }

    class T212GeneratorConfigurator implements Configurator<T212Generator> {
        @Override
        public void config(T212Generator generator) {
            T212Configurator.this.configure(generator);
        }
    }

    class PackLevelSerializerConfigurator implements Configurator<PackLevelSerializer> {
        @Override
        public void config(PackLevelSerializer serializer) {
            T212Configurator.this.configure(serializer);
        }
    }

    class CpDataLevelMapDataSerializerConfigurator implements Configurator<CpDataLevelMapDataSerializer> {
        @Override
        public void config(CpDataLevelMapDataSerializer serializer) {
            T212Configurator.this.configure(serializer);
        }
    }

    class DataSerializerConfigurator implements Configurator<DataSerializer> {
        @Override
        public void config(DataSerializer serializer) {
            T212Configurator.this.configure(serializer);
        }
    }

    class DataReverseConverterConfigurator implements Configurator<DataReverseConverter> {
        @Override
        public void config(DataReverseConverter converter) {
            T212Configurator.this.configure(converter);
        }
    }


    @Override
    public Collection<Configurator> configurators() {
        return Stream.of(
                new SegmentParserConfigurator(),
                new T212ParserConfigurator(),
                new PackLevelDeserializerConfigurator(),
                new DataLevelMapDeserializerConfigurator(),
                new CpDataLevelMapDeserializerConfigurator(),
                new DataDeserializerConfigurator(),
                new DataConverterConfigurator(),

                new SegmentGeneratorConfigurator(),
                new T212GeneratorConfigurator(),
                new PackLevelSerializerConfigurator(),
                new CpDataLevelMapDataSerializerConfigurator(),
                new DataSerializerConfigurator(),
                new DataReverseConverterConfigurator()
        )
                .collect(Collectors.toSet());
    }

    /**
     * 泛型方法实现
     *
     * @param parser
     * @see Configurator#config(Object)
     */
    public void configure(SegmentParser parser) {
        if (parser.currentToken() == null) {
            parser.initToken();
        }
        parser.setParserFeature(segmentParserFeature);
    }

    /**
     * 泛型方法实现
     *
     * @param parser
     * @see Configurator#config(Object)
     */
    public void configure(T212Parser parser) {
        parser.setParserFeature(parserFeature);
    }

    /**
     * 泛型方法实现
     *
     * @param deserializer
     * @see Configurator#config(Object)
     */
    public void configure(PackLevelDeserializer deserializer) {
        deserializer.setVerifyFeature(verifyFeature);
        deserializer.setParserConfigurator(this::configure);
    }

    /**
     * 泛型方法实现
     *
     * @param deserializer
     * @see Configurator#config(Object)
     */
    public void configure(DataLevelMapDeserializer deserializer) {
        deserializer.setVerifyFeature(verifyFeature);
        deserializer.setValidator(validator);
        deserializer.setSegmentParserConfigurator(this::configure);
        deserializer.setSegmentDeserializer(new StringMapSegmentDeserializer());
    }

    /**
     * 泛型方法实现
     *
     * @param deserializer
     * @see Configurator#config(Object)
     */
    public void configure(CpDataLevelMapDeserializer deserializer) {
        deserializer.setVerifyFeature(verifyFeature);
        deserializer.setValidator(validator);
        deserializer.setSegmentParserConfigurator(this::configure);
        deserializer.setSegmentDeserializer(new MapSegmentDeserializer());
    }

    /**
     * 泛型方法实现
     *
     * @param deserializer
     * @see Configurator#config(Object)
     */
    public void configure(DataDeserializer deserializer) {
        deserializer.setVerifyFeature(verifyFeature);
        deserializer.setValidator(validator);
        deserializer.setSegmentParserConfigurator(this::configure);
        deserializer.setSegmentDeserializer(new MapSegmentDeserializer());
        deserializer.setDataConverterConfigurator(this::configure);
    }

    /**
     * 泛型方法实现
     *
     * @param dataConverter
     * @see Configurator#config(Object)
     */
    public void configure(DataConverter dataConverter) {
        ObjectMapper objectMapper = this.objectMapper;
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        dataConverter.setObjectMapper(objectMapper);
    }


    /**
     * 泛型方法实现
     *
     * @param generator
     * @see Configurator#config(Object)
     */
    private void configure(SegmentGenerator generator) {
        if (generator.nextToken() == null) {
            generator.initToken();
        }
        generator.setGeneratorFeature(segmentGeneratorFeature);
    }

    /**
     * 泛型方法实现
     *
     * @param generator
     * @see Configurator#config(Object)
     */
    private void configure(T212Generator generator) {
        generator.setGeneratorFeature(generatorFeature);
    }

    /**
     * 泛型方法实现
     *
     * @param serializer
     * @see Configurator#config(Object)
     */
    private void configure(PackLevelSerializer serializer) {
        serializer.setVerifyFeature(verifyFeature);
        serializer.setGeneratorConfigurator(this::configure);
    }

    /**
     * 泛型方法实现
     *
     * @param serializer
     * @see Configurator#config(Object)
     */
    public void configure(CpDataLevelMapDataSerializer serializer) {
        serializer.setVerifyFeature(verifyFeature);
        serializer.setValidator(validator);
        serializer.setSegmentGeneratorConfigurator(this::configure);
        serializer.setSegmentSerializer(new MapSegmentSerializer(new T212CpMapPathValueSegmentSerializer()));
    }

    /**
     * 泛型方法实现
     *
     * @param serializer
     * @see Configurator#config(Object)
     */
    private void configure(DataSerializer serializer) {
        serializer.setVerifyFeature(verifyFeature);
        serializer.setValidator(validator);
        serializer.setSegmentGeneratorConfigurator(this::configure);
        serializer.setSegmentSerializer(new MapSegmentSerializer(new T212CpMapPathValueSegmentSerializer()));
        serializer.setDataReverseConverterConfigurator(this::configure);
    }

    /**
     * 泛型方法实现
     *
     * @param dataReverseConverter
     * @see Configurator#config(Object)
     */
    private void configure(DataReverseConverter dataReverseConverter) {
        ObjectMapper objectMapper = this.objectMapper;
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        dataReverseConverter.setObjectMapper(objectMapper);
    }

}
