package com.alltobs.hj212.translator;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface CodeMatch extends CodePattern {

    boolean match(String code);
}
