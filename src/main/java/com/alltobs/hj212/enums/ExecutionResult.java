package com.alltobs.hj212.enums;

import com.alltobs.hj212.translator.ValueLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenQi
 */
@Getter
@AllArgsConstructor
public enum ExecutionResult implements ValueLabel {

    _1("执行成功"),
    _2("执行失败，但不知道原因"),
    _3("命令请求条件错误"),
    _4("通讯超时"),
    _5("系统繁忙不能执行"),
    _6("系统故障"),
    _100("没有数据");

    private final String value;
    private final String label;

    ExecutionResult(String label) {
        this.value = name().substring(1);
        this.label = label;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String label() {
        return label;
    }
}
