package com.alltobs.hj212.translator;

import com.alltobs.hj212.exception.UnsupportedDataTypeException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 功能: 旧国标->新国标 或者 新国标->旧国标的翻译
 *
 * @author chenQi
 */
@Slf4j
public enum H212Translator {
    /**
     *
     */
    I;

    private Map<Enum, Pattern> compilePattern = Collections.synchronizedMap(new LinkedHashMap<>());

    private Map<Class<? extends Enum>, Object> expandCode = Collections.synchronizedMap(new LinkedHashMap<>());

    /**
     * 翻译
     *
     * @param codeType codeType
     * @param code     code
     * @return 翻译内容
     * @throws UnsupportedDataTypeException 不支持得数据类型
     * @throws ClassNotFoundException       文件未找到
     */
    public String translation(Class<? extends Enum> codeType, String code) throws UnsupportedDataTypeException, ClassNotFoundException {
        if (!expandCode.containsKey(codeType)) {
            register(codeType);
        }
        Object codeMeanMap = expandCode.get(codeType);

        if (codeMeanMap instanceof Map) {
            Map<String, CodeMean> map = (Map) codeMeanMap;
            return Optional.ofNullable(map.get(code))
                    .orElseThrow(ClassNotFoundException::new)
                    .mean();
        } else if (codeMeanMap instanceof List) {
            Set<CodeMatch> set = (Set<CodeMatch>) codeMeanMap;
            return set.stream()
                    .filter(cm -> cm.match(code))
                    .findAny()
                    .orElseThrow(ClassNotFoundException::new)
                    .mean();
        }
        return null;
    }

    /**
     * 注册
     *
     * @param codeType 类型
     * @return 是否成功
     * @throws UnsupportedDataTypeException 不支持得数据类型
     */
    public boolean register(Class<? extends Enum> codeType) throws UnsupportedDataTypeException {
        if (expandCode.containsKey(codeType)) {
            throw new UnsupportedOperationException("Repeat registration");
        }

        Optional selfCompiled = Stream.of(codeType.getInterfaces())
                .filter(i -> i.equals(CodeMatch.class))
                .findAny();
        if (selfCompiled.isPresent()) {
            Set<CodeMatch> map = Stream.of(codeType.getEnumConstants())
                    .map(cm -> (CodeMatch) cm)
                    .collect(Collectors.toCollection(() -> new TreeSet<>((o1, o2) -> {
                        int i = o1.order() - o2.order();
                        if (i == 0) {
                            i++;
                        }
                        return i;
                    })));
            expandCode.put(codeType, map);
            return true;
        }

        Optional needCompile = Stream.of(codeType.getInterfaces())
                .filter(i -> i.equals(CodePattern.class))
                .findAny();
        if (needCompile.isPresent()) {
            Set<CodePattern> map = Stream.of(codeType.getEnumConstants())
                    .map(cm -> (CodePattern) cm)
                    .map(CodePatternMatchAdapter::adaptation)
                    .collect(Collectors.toCollection(() -> new TreeSet<>((o1, o2) -> {
                        int i = o1.order() - o2.order();
                        if (i == 0) {
                            i++;
                        }
                        return i;
                    })));
            expandCode.put(codeType, map);
            return true;
        }


        //noinspection ResultOfMethodCallIgnored
        Stream.of(codeType.getInterfaces())
                .filter(i -> i.equals(CodeMean.class))
                .findAny()
                .orElseThrow(UnsupportedDataTypeException::new);
        Map<String, CodeMean> map = Stream.of(codeType.getEnumConstants())
                .map(cm -> (CodeMean) cm)
                .collect(Collectors.toMap(CodeMean::code, Function.identity(), (k1, k2) -> {
                    return k2;
                }));
        expandCode.put(codeType, map);
        return true;
    }

    /**
     * 扩展
     *
     * @param codeType  类型
     * @param code      代码
     * @param mean      含义
     * @param predicate 自定义匹配规则
     * @return 是否成功
     */
    public boolean expand(Class<? extends Enum> codeType, String code, String mean, Predicate<String> predicate) throws UnsupportedDataTypeException, ClassNotFoundException {
        if (!expandCode.containsKey(codeType)) {
            register(codeType);
        }
        Object codeMeanMap = expandCode.get(codeType);
        String finalCode = code;
        if (codeMeanMap instanceof Map) {
            Map<String, CodeMean> map = (Map) codeMeanMap;
            CodeMean cm = new CodeMean() {
                @Override
                public String code() {
                    return finalCode;
                }

                @Override
                public String mean() {
                    return mean;
                }
            };
            map.put(code, cm);
        } else if (codeMeanMap instanceof List) {
            Set set = (Set) codeMeanMap;
            CodeMatch cm = new CodeMatch() {
                @Override
                public boolean match(String code) {
                    return predicate.test(code);
                }

                @Override
                public String pattern() {
                    return null;
                }

                @Override
                public int order() {
                    return -1;
                }

                @Override
                public String code() {
                    return finalCode;
                }

                @Override
                public String mean() {
                    return mean;
                }
            };
            set.add(cm);
        }


        return true;
    }

    private static class CodePatternMatchAdapter implements CodeMatch {

        public static CodePatternMatchAdapter adaptation(CodePattern codePattern) {
            CodePatternMatchAdapter adapter = new CodePatternMatchAdapter();
            adapter.codePattern = codePattern;
            adapter.pattern = Pattern.compile(codePattern.pattern());
            return adapter;
        }

        private CodePattern codePattern;
        private Pattern pattern;

        @Override
        public boolean match(String code) {
            return pattern.asPredicate().test(code);
        }

        @Override
        public String pattern() {
            return codePattern.pattern();
        }

        @Override
        public int order() {
            return -1;
        }

        @Override
        public String code() {
            return codePattern.code();
        }

        @Override
        public String mean() {
            return codePattern.mean();
        }
    }

}
