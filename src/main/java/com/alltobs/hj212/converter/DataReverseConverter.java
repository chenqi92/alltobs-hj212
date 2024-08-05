package com.alltobs.hj212.converter;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.*;
import com.alltobs.hj212.model.verify.T212Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 转换器
 * 将
 *
 * @see HjData
 * 转为T212Map
 * Created by xiaoyao9184 on 2018/2/25.
 */
public class DataReverseConverter implements Converter<HjData, T212Map<String, Object>>, Configured<DataReverseConverter> {

    private ObjectMapper objectMapper;


    /**
     * 转换 现场端
     *
     * @param map
     * @return
     */
    private Map<String, Map<String, String>> convertLiveSide(Map<String, LiveSide> map) {
        if (map == null) {
            return null;
        }
        return map.entrySet().stream()
                .map(kv -> {
                    Map<String, String> value = objectMapper.convertValue(kv.getValue(), Map.class);
                    return new AbstractMap.SimpleEntry<>(kv.getKey(), value);
                })
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
    }

    /**
     * 转换 设备
     *
     * @param map
     * @return
     */
    private Map<String, Map<String, String>> convertDevice(Map<String, Device> map) {
        if (map == null) {
            return null;
        }
        return map.entrySet().stream()
                .map(kv -> {
                    String key = "SB" + kv.getKey();
                    Map<String, String> value = objectMapper.convertValue(kv.getValue(), Map.class);
                    return new AbstractMap.SimpleEntry<>(key, value);
                })
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
    }

    /**
     * 转换 污染因子
     *
     * @param map
     * @return
     */
    private Map<String, Map<String, String>> convertPollution(Map<String, Pollution> map) {
        if (map == null) {
            return null;
        }
        return map.entrySet().stream()
                .map(kv -> {
                    Map<String, String> value = objectMapper.convertValue(kv.getValue(), Map.class);
                    return new AbstractMap.SimpleEntry<>(kv.getKey(), value);
                })
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue,
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        LinkedHashMap::new
                ));
    }

    /**
     * 转换 数据区
     *
     * @param cp
     * @return
     */
    private T212Map<String, Object> convertDataLevel(CpData cp) {
        Map map = objectMapper
                .convertValue(cp, Map.class);

        if (cp.getDataFlag() != null &&
                !cp.getDataFlag().isEmpty()) {
            String flag = convertDataFlag(cp.getDataFlag());
            map.put(HjData.FLAG, flag);
        }

        if (cp.getDevice() != null &&
                !cp.getDevice().isEmpty()) {
            map.remove(CpData.DEVICE);
            Map<String, Map<String, String>> device = convertDevice(cp.getDevice());
            map.putAll(device);
        }

        if (cp.getLiveSide() != null &&
                !cp.getLiveSide().isEmpty()) {
            map.remove(CpData.LIVESIDE);
            Map<String, Map<String, String>> liveSide = convertLiveSide(cp.getLiveSide());
            map.putAll(liveSide);
        }

        if (cp.getPollution() != null &&
                !cp.getPollution().isEmpty()) {
            map.remove(CpData.POLLUTION);
            Map<String, Map<String, String>> pollution = convertPollution(cp.getPollution());
            map.putAll(pollution);
        }

        return T212Map.createCpDataLevel(map);
    }

    /**
     * 转换 数据段标记
     *
     * @param flag
     * @return
     */
    private String convertDataFlag(List<HjDataFlag> flag) {
        int i = 0;
        if (flag == null) {
            return "";
        }
        for (HjDataFlag dataFlag : flag) {
            i = dataFlag.getBit() & i;
        }
        return Integer.toString(i);
    }

    /**
     * 转换 数据段
     *
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    private T212Map<String, Object> convertDataLevel(HjData data) {
        Map map = objectMapper
                .convertValue(data, Map.class);
        if (data.getDataFlag() != null &&
                !data.getDataFlag().isEmpty()) {
            map.remove(HjData.FLAG);
            String flag = convertDataFlag(data.getDataFlag());
            map.put(HjData.FLAG, flag);
        }

        if (data.getCp() != null) {
            Map<String, Object> cpMap = convertDataLevel(data.getCp());
            map.put(HjData.CP, cpMap);
        }

        return T212Map.createCpDataLevel(map);
    }

    @Override
    public T212Map<String, Object> convert(HjData data) {
        return convertDataLevel(data);
    }

    @Override
    public void configured(Configurator<DataReverseConverter> by) {
        by.config(this);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
