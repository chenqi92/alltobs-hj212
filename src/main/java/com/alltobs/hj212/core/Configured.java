package com.alltobs.hj212.core;

/**
 * 功能:
 *
 * @author chenQi
 */
public interface Configured<Target> {

    /**
     * 被配置
     * @param by 目标配置器
     */
    void configured(Configurator<Target> by);
}
