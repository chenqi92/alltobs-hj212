package com.alltobs.hj212.deser;

import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.format.T212Parser;

import java.io.IOException;

/**
 * 功能: T212反序列化器
 *
 * @author chenQi
 */
public interface T212Deserializer<Target> {

    Target deserialize(T212Parser parser) throws IOException, T212FormatException;
}
