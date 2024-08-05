package com.alltobs.hj212.exception;

import java.io.IOException;

/**
 * 类 UnsupportedDataTypeException
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/17 11:34
 */
public class UnsupportedDataTypeException extends IOException {

    public UnsupportedDataTypeException() {
    }

    public UnsupportedDataTypeException(String s) {
        super(s);
    }
}
