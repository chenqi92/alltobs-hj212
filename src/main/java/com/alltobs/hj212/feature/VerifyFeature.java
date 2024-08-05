package com.alltobs.hj212.feature;

/**
 * 验证特性
 * Created by xiaoyao9184 on 2018/1/3.
 */
public enum VerifyFeature implements Feature {

    /**
     * 数据区长度
     */
    DATA_LEN_RANGE(false),

    /**
     * 数据区CRC
     */
    DATA_CRC(false),

    /**
     * 启动校验
     */
    USE_VERIFICATION(false),

    /**
     * 校验失败报错
     */
    THROW_ERROR_VERIFICATION_FAILED(true);

    private final boolean _defaultState;
    private final int _mask;

    VerifyFeature(boolean defaultState) {
        _defaultState = defaultState;
        _mask = (1 << ordinal());
    }

    @Override
    public boolean enabledByDefault() {
        return _defaultState;
    }

    @Override
    public int getMask() {
        return _mask;
    }

    @Override
    public boolean enabledIn(int flags) {
        return (flags & _mask) != 0;
    }

}
