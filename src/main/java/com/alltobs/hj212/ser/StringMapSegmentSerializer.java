package com.alltobs.hj212.ser;

import com.alltobs.hj212.config.SegmentGenerator;
import com.alltobs.hj212.exception.SegmentFormatException;

import java.io.IOException;
import java.util.Map;

/**
 * 功能:
 *
 * @author chenQi
 */
public class StringMapSegmentSerializer
        implements SegmentSerializer<Map<String, String>> {

    @Override
    public void serialize(SegmentGenerator generator, Map<String, String> data) throws IOException, SegmentFormatException {
        if (generator.nextToken() == null) {
            generator.initToken();
        }
        writeMap(generator, data);
    }

    private void writeMap(SegmentGenerator generator, Map<String, String> result) throws IOException, SegmentFormatException {
        for (Map.Entry<String, String> kv : result.entrySet()) {
            generator.writeKey(kv.getKey());
            generator.writeValue(kv.getValue());
        }
    }

}
