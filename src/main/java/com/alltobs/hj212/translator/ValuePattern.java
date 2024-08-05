package com.alltobs.hj212.translator;

/**
 * @author ChenQi
 */
public interface ValuePattern extends ValueLabel {

    /**
     * 正则匹配
     *
     * @return String
     */
    String pattern();

    /**
     * 排序
     *
     * @return String
     */
    int order();

}
