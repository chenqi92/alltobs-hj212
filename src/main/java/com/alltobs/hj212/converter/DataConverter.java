package com.alltobs.hj212.converter;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.enums.HjDataFlag;
import com.alltobs.hj212.model.*;
import com.alltobs.hj212.model.verify.DataElement;
import com.alltobs.hj212.model.verify.T212Map;
import com.alltobs.hj212.utils.T212Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 转换器
 * 将T212Map转为
 *
 * @see HjData
 * Created by xiaoyao9184 on 2017/12/15.
 */
public class DataConverter
        implements Converter<T212Map<String, Object>, HjData>,
        Configured<DataConverter> {

    private static String SPLIT = "-";
    private static String SB = "SB";
    private static String REGEX_DEVICE = "SB.*-RS$|SB.*-RT$";
    private static String REGEX_LIVE_SIDE = ".*-Info$|.*-SN$";
    private static String REGEX_POLLUTION = ".*-.*";

    private ObjectMapper objectMapper;

    private Predicate<String> predicateDevice;
    private Predicate<String> predicateLiveSide;
    private Predicate<String> predicatePollution;


    public DataConverter() {
        predicateDevice = Pattern.compile(REGEX_DEVICE).asPredicate();
        predicateLiveSide = Pattern.compile(REGEX_LIVE_SIDE).asPredicate();
        predicatePollution = Pattern.compile(REGEX_POLLUTION).asPredicate();
    }

    /**
     * 根据Key中的分隔符分组
     *
     * @param map   Map
     * @param split 分隔符
     * @return 分组Map
     */
    private Map<String, Map<String, String>> groupBySplitKey(Map<String, String> map, String split) {
        Map<String, Map<String, String>> result = map.entrySet().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getKey().split(split)[0],
                        Collectors.toMap(
                                kv -> kv.getKey().split(split)[1],
                                Map.Entry::getValue
                        )
                ));
        return result;
    }

    /**
     * 转换 现场端
     *
     * @param map
     * @return
     */
    private Map<String, LiveSide> convertLiveSide(Map<String, String> map) {
        return groupBySplitKey(map, SPLIT)
                .entrySet()
                .stream()
                .map(kv -> {
                    LiveSide liveSide = objectMapper.convertValue(kv.getValue(), LiveSide.class);
                    return new AbstractMap.SimpleEntry<>(kv.getKey(), liveSide);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    /**
     * 转换 设备
     *
     * @param map
     * @return
     */
    private Map<String, Device> convertDevice(Map<String, String> map) {
        return groupBySplitKey(map, SPLIT)
                .entrySet()
                .stream()
                .map(kv -> {
                    Device device = objectMapper.convertValue(kv.getValue(), Device.class);
                    return new AbstractMap.SimpleEntry<>(kv.getKey().replace(SB, ""), device);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    /**
     * 转换 污染因子
     *
     * @param map
     * @return
     */
    private Map<String, Pollution> convertPollution(Map<String, String> map) {
        return groupBySplitKey(map, SPLIT)
                .entrySet()
                .stream()
                .map(kv -> {
                    Pollution pollution = objectMapper.convertValue(kv.getValue(), Pollution.class);
                    return new AbstractMap.SimpleEntry<>(kv.getKey(), pollution);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }


    /**
     * 过滤Map中的Entry
     *
     * @param map
     * @param predicate 过滤逻辑
     * @return
     */
    private Map<String, String> filter(Map<String, String> map, Predicate<String> predicate) {
        return map.entrySet()
                .stream()
                .filter(kv -> T212Util.isNotEmpty(kv.getValue()))
                .filter(kv -> predicate.test(kv.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    /**
     * 转换 数据区
     *
     * @param map
     * @return
     */
    private Map<String, Object> convertDataLevel(Map<String, String> map) {
        Map<String, Object> cp = new HashMap<>();
        //v2017 entry
        Map<String, String> d = filter(map, predicateDevice);
        if (!d.isEmpty()) {
            Map<String, Device> deviceMap = convertDevice(d);
            cp.put(CpData.DEVICE, deviceMap);
            d.keySet().forEach(map::remove);
        }

        Map<String, String> ls = filter(map, predicateLiveSide);
        if (!ls.isEmpty()) {
            Map<String, LiveSide> liveSideMap = convertLiveSide(ls);
            cp.put(CpData.LIVESIDE, liveSideMap);
            ls.keySet().forEach(map::remove);
        }

        //v2005 entry
        String flag = map.get(DataElement.Flag.name());
        if (T212Util.isNotEmpty(flag)) {
            List<HjDataFlag> dataFlags = convertDataFlag(flag);
            map.remove(DataElement.Flag.name());
            cp.put(HjData.FLAG, dataFlags);
        }

        Map<String, String> p = filter(map, predicatePollution);
        if (!p.isEmpty()) {
            Map<String, Pollution> pollutionMap = convertPollution(p);
            cp.put(CpData.POLLUTION, pollutionMap);
            p.keySet().forEach(map::remove);
        }

        //common entry must keep
        cp.putAll(map);
        return cp;
    }

    /**
     * 转换 数据段标记
     *
     * @param flag
     * @return
     */
    private List<HjDataFlag> convertDataFlag(String flag) {
        if (T212Util.isNotEmpty(flag)) {
            int i = Integer.parseInt(flag);

            return Stream.of(HjDataFlag.values())
                    .filter(sf -> sf.isMarked(i))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 转换 数据段
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    private HjData convertDataLevel(T212Map<String, Object> map) {
        String flag = (String) map.get(DataElement.Flag.name());
        if (T212Util.isNotEmpty(flag)) {
            List<HjDataFlag> flagList = convertDataFlag(flag);
            //replace
            map.put(HjData.FLAG, flagList);
        }

        Map<String, String> cp = (Map<String, String>) map.get(DataElement.CP.name());
        if (T212Util.isNotEmpty(cp)) {
            Map<String, Object> cpMap = convertDataLevel(cp);
            //replace
            map.put(HjData.CP, cpMap);
        }

        return objectMapper
                .convertValue(map, HjData.class);
    }

    @Override
    public HjData convert(T212Map<String, Object> map) {
        return convertDataLevel(map);
    }

    @Override
    public void configured(Configurator<DataConverter> by) {
        by.config(this);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
