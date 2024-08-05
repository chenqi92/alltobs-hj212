package com.alltobs.hj212.core;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface Configurator<Target> {

    /**
     * 配置
     * @param target 配置目标
     */
    void config(Target target);
}
