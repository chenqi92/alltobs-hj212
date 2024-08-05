package com.alltobs.hj212.ser;

import com.alltobs.hj212.config.SegmentGenerator;
import com.alltobs.hj212.exception.SegmentFormatException;

import java.io.IOException;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface SegmentSerializer<Target> {

    void serialize(SegmentGenerator generator, Target target) throws IOException, SegmentFormatException;
}
