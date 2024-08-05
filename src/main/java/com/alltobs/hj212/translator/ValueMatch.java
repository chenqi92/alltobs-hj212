package com.alltobs.hj212.translator;

/**
 * @author ChenQi
 */
public interface ValueMatch extends ValuePattern {

    /**
     * 是否匹配
     *
     * @param value 枚举/字典的value
     * @return boolean
     */
    boolean match(String value);
}
