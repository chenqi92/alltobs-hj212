package com.alltobs.hj212.converter;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface Converter<SRC,TAR> {

    TAR convert(SRC src) throws Exception;
}

