package com.alltobs.hj212.ser;

import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.format.T212Generator;

import java.io.IOException;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface T212Serializer<Target> {

    void serialize(T212Generator generator, Target target) throws IOException, T212FormatException;
}

