package com.alltobs.hj212.deser;

import com.alltobs.hj212.config.SegmentParser;
import com.alltobs.hj212.exception.SegmentFormatException;

import java.io.IOException;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface SegmentDeserializer<Target> {

    Target deserialize(SegmentParser parser) throws IOException, SegmentFormatException;
}
