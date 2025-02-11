package com.alltobs.hj212.config;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.core.ReaderStream;
import com.alltobs.hj212.feature.SegmentParserFeature;
import lombok.Setter;

import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 功能:
 *
 * @author chenQi
 */
public class SegmentParser implements Closeable, Configured<SegmentParser> {

    protected PushbackReader reader;
    @Setter
    private int parserFeature;
    private SegmentToken currentToken;
    private Stack<String> path;

    public SegmentParser(Reader reader) {
        this.reader = new PushbackReader(reader, 3);
        this.path = new Stack<>();
    }

    /**
     * 读取KEY
     *
     * @return KEY
     * @throws IOException
     */
    public String readKey() throws IOException {
        return readPathKey(false);
    }

    /**
     * 读取一部分KEY
     *
     * @return 部分KEY
     * @throws IOException
     */
    public String readPathKey() throws IOException {
        return readPathKey(true);
    }

    /**
     * 读取KEY
     *
     * @param supportSubKey true：遇到‘-’结束
     * @return KEY
     * @throws IOException
     */
    private String readPathKey(boolean supportSubKey) throws IOException {
        //当前Token
        switch (currentToken) {
            case END_KEY:
                throw new IOException("Cant read key after END_KEY token!");
            case END_OBJECT_VALUE:
                //“&&”字符串后面跟着“,”“;”认为是相同的，需要读出来，
                // 避免被认为是当前是孤立KEY
                ReaderStream.of(reader)
                        .next()
                        .when(SegmentToken.END_SUB_ENTRY::isSame)
                        .then(() -> currentToken = SegmentToken.END_SUB_ENTRY)
                        .when(SegmentToken.END_ENTRY::isSame)
                        .then(() -> currentToken = SegmentToken.END_ENTRY)
                        .done()
                        .match();
                break;
        }

        CharBuffer buffer = CharBuffer.allocate(20);

        //之后的Token
        int len = ReaderStream.of(reader)
                .next()
                .when(SegmentToken.NOT_AVAILABLE::isSame)
                .then(() -> currentToken = SegmentToken.NOT_AVAILABLE)
                //perfect
                .when(c -> supportSubKey && SegmentToken.END_PART_KEY.isSame(c))
                .then(() -> currentToken = SegmentToken.END_PART_KEY)
                .when(SegmentToken.END_KEY::isSame)
                .then()
                .next(2)
                .when(SegmentToken.START_OBJECT_VALUE::isSame)
                .then(() -> currentToken = SegmentToken.START_OBJECT_VALUE)
                .done()
                .back()
                .then(() -> currentToken = SegmentToken.END_KEY)
                .when(SegmentToken.END_SUB_ENTRY::isSame)
                .then(() -> {
                    //Missing '=' core value
                    if (!SegmentParserFeature.ALLOW_ISOLATED_KEY.enabledIn(parserFeature)) {
                        throw new IOException("Missing '=' between key and (null)value");
                    }
                    //NULL Value
                    currentToken = SegmentToken.END_SUB_ENTRY;
                })
                .when(SegmentToken.END_ENTRY::isSame)
                .then(() -> {
                    //Missing '=' core value
                    if (!SegmentParserFeature.ALLOW_ISOLATED_KEY.enabledIn(parserFeature)) {
                        throw new IOException("Missing '=' between key and (null)value");
                    }
                    //NULL Value
                    currentToken = SegmentToken.END_ENTRY;
                })
                //key&&
                //也许是缺少= START_OBJECT_VALUE
                //也许是NullValue END_OBJECT_VALUE
                .when(SegmentToken.START_OBJECT_VALUE::isStart)
                .then()
                .next()
                .when(SegmentToken.START_OBJECT_VALUE::isStart)
                .then(() -> {
                    if (path.empty()) {
                        //Missing '='
                        if (!SegmentParserFeature.ALLOW_KEY_NOT_CLOSED.enabledIn(parserFeature)) {
                            throw new IOException("Missing '=' between key and (object)value");
                        }
                        //Object Value
                        currentToken = SegmentToken.START_OBJECT_VALUE;
                    } else {
                        //NULL Value
                        currentToken = SegmentToken.END_OBJECT_VALUE;
                    }
                })
                .done()
                .back()
                .then(() -> {
                    if (!SegmentParserFeature.IGNORE_INVAILD_SYMBOL.enabledIn(parserFeature)) {
                        throw new IOException("Invaild symbol '&' in key");
                    }
                    //Ignore Invaild symbol
                })
                .done()
                .read(buffer);

        if (currentToken == SegmentToken.END_OBJECT_VALUE) {
            path.pop();
        }

        if (len == 0) {
            return null;
        }

        buffer.rewind();
        String result = buffer.toString().substring(0, len);
        if (currentToken == SegmentToken.START_OBJECT_VALUE) {
            path.push(result);
        }
        return result;
    }

    /**
     * 读取VALUE
     *
     * @return VALUE
     * @throws IOException
     */
    public String readValue() throws IOException {
        return readValue(false);
    }

    /**
     * 读取对象VALUE
     *
     * @return 对象VALUE
     * @throws IOException
     */
    public String readObjectValue() throws IOException {
        return readValue(true);
    }


    /**
     * 读取VALUE
     *
     * @param onlyObject true：仅遇到‘&&’结束
     * @return VALUE
     * @throws IOException
     */
    private String readValue(boolean onlyObject) throws IOException {
        //开启常规方式闭合Value
        boolean basic = !onlyObject;
        //当前Token
        switch (currentToken) {
            case END_ENTRY:
            case END_SUB_ENTRY:
            case END_PART_KEY:
            case END_OBJECT_VALUE:
                throw new IOException("Cant read value after " + currentToken.name() + " token!");
            case START_OBJECT_VALUE:
                if (onlyObject) {
                    //当前的值不是Object
                    basic = false;
                } else {
                    throw new IOException("Cant read base value after " + currentToken.name() + " token!");
                }
                break;
        }

        AtomicReference<Integer> deep = new AtomicReference<>(0);
        // @formatter:off
        CharBuffer buffer = CharBuffer.allocate(basic ? 50 : 1024);
        boolean finalBasic = basic;
        int len = ReaderStream.of(reader)
                .next()
                .when(SegmentToken.NOT_AVAILABLE::isSame)
                .then(() -> currentToken = SegmentToken.NULL_VALUE)
                //常规方式闭合
                .when(c -> finalBasic && SegmentToken.END_SUB_ENTRY.isSame(c))
                .then(() -> currentToken = SegmentToken.END_SUB_ENTRY)//break
                .when(c -> finalBasic && SegmentToken.END_ENTRY.isSame(c))
                .then(() -> currentToken = SegmentToken.END_ENTRY)//break

                //标记
                .when(SegmentToken.END_KEY::isSame)
                .then()
                .next(2)
                .when(SegmentToken.START_OBJECT_VALUE::isSame)
                .then(() -> {
                    //忽略4个‘&’
                    deep.getAndSet(deep.get() + 4);
//                                currentToken = START_OBJECT_VALUE;
//                                return Optional.empty();
                })
                .back()//回滚
                .back()

//                    .when(c -> currentToken == START_OBJECT_VALUE && START_OBJECT_VALUE.isStart(c))
//                    .then()
//                    .skip()

//                    .when(c -> currentToken != START_OBJECT_VALUE && END_OBJECT_VALUE.isStart(c))

                //value&&
                //也许是内部object START_OBJECT_VALUE
                //也许是闭合内部object END_OBJECT_VALUE
                //也许是闭合 END_OBJECT_VALUE
                .when(c -> SegmentToken.START_OBJECT_VALUE.isStart(c))
                .then()
                .next()
                .when(SegmentToken.START_OBJECT_VALUE::isStart)
                .then(() -> {
                    if (deep.get() == 0) {
                        currentToken = SegmentToken.END_OBJECT_VALUE;
                        return Optional.of(true);
                    } else {
//                                    deep.getAndSet(deep.get() - 1);
                        return Optional.empty();
                    }
                })
                .done()
                .back()
                .then(() -> {
                    if (deep.get() != 0) {
                        //被忽略
                        deep.getAndSet(deep.get() - 1);
                        return Optional.empty();
                    }

                    if (!SegmentParserFeature.IGNORE_INVAILD_SYMBOL.enabledIn(parserFeature)) {

                    }
                    //Ignore Invaild symbol
                    return Optional.empty();
                })
                .done()
                .read(buffer);
        // @formatter:on
        if (currentToken == SegmentToken.END_OBJECT_VALUE) {
            path.pop();
        }

        if (len == 0) {
            return null;
        }

        buffer.rewind();
        //字符串去掉"\0"后缀
        String result = buffer.toString().substring(0, len);
        return result;
//
//
//        mode = SegmentMode.VALUE;
//        StringWriter sw = new StringWriter();
//        int i;
//        while((i = reader.next()) != -1) {
//            char c = (char)i;
//            if(SegmentToken.END_SUB_ENTRY.isSame(c)){
//                //End of Value in Sub Entry
//                currentToken = SegmentToken.END_SUB_ENTRY;
//                break;
//            }else if(SegmentToken.END_ENTRY.isSame(c)) {
//                //End of Value in Entry
//                currentToken = SegmentToken.END_ENTRY;
//                break;
//            }else if(SegmentToken.END_OBJECT_VALUE.isStart(c)){
//                int finalI = i;
//                if(readIf(nc -> nc == finalI)){
//                    //End of Value in Object
//                    currentToken = SegmentToken.END_OBJECT_VALUE;
//                    break;
//                }
////                reader.mark(-0);
////                if(reader.next() == i){
////                    //End of Value in Object
////                    currentToken = SegmentToken.END_OBJECT_VALUE;
////                    break;
////                }
////                reader.reset();
//
//                if(!IGNORE_INVAILD_SYMBOL.enabledIn(parserFeature)){
//
//                }
//                //Ignore Invaild symbol
//                continue;
//            }else {
//                reader.mark(-0);
//                char[] c2 = new char[2];
//                reader.next(c2);
//                if(SegmentToken.END_OBJECT_VALUE.isSame(c2)){
//                    currentToken = SegmentToken.START_OBJECT_VALUE;
//                }else{
//                    reader.reset();
//                    currentToken = SegmentToken.START_VALUE;
//                }
//
//
//            }
//            sw.append(c);
//        }
//        return sw.toString();
    }


    /**
     * 当前Token
     *
     * @return SegmentToken
     */
    public SegmentToken currentToken() {
        return currentToken;
    }

    public void initToken() {
        currentToken = SegmentToken.START_OBJECT_VALUE;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configured(Configurator<SegmentParser> by) {
        by.config(this);
    }
}
