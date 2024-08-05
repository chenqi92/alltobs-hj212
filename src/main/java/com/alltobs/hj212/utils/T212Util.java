package com.alltobs.hj212.utils;

import java.util.Map;

/**
 * 类 T212Util
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/17 11:41
 */
public class T212Util {

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }
}
