package com.alltobs.hj212.translator;

/**
 * @author ChenQi
 */
public interface ValueLabel {

    /**
     * 枚举、字典值/编码
     *
     * @return String
     */
    String value();

    /**
     * 枚举、字典值的意义
     *
     * @return String
     */
    String label();
}
