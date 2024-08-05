package com.alltobs.hj212.format;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.ParserFeature;
import com.alltobs.hj212.model.verify.PacketElement;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * 功能: T212通信包解析器
 *
 * @author chenQi
 */
public class T212Parser implements Configured<T212Parser>, Closeable {

    public static char[] HEADER = new char[]{'#', '#'};
    public static char[] FOOTER = new char[]{'\r', '\n'};

    protected Reader reader;

    private int parserFeature;

    /**
     * TEMP
     */
    private int count;

    public T212Parser(Reader reader) {
        this.reader = reader;
    }

    /**
     * 设置解析特性
     *
     * @param parserFeature 解析特性
     */
    public void setParserFeature(int parserFeature) {
        this.parserFeature = parserFeature;
    }


    /**
     * 读取 包头
     *
     * @return chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement #HEADER
     */
    public char[] readHeader() throws T212FormatException, IOException {
        char[] header = new char[2];
        count = reader.read(header);
        VerifyUtil.verifyLen(count, 2, PacketElement.HEADER);
        if (ParserFeature.HEADER_CONSTANT.enabledIn(parserFeature)) {
            VerifyUtil.verifyChar(header, HEADER, PacketElement.HEADER);
        }
        return header;
    }

    /**
     * 读取 数据段长度
     *
     * @return chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA_LEN
     */
    public char[] readDataLen() throws T212FormatException, IOException {
        char[] len = new char[4];
        count = reader.read(len);
        VerifyUtil.verifyLen(count, len.length, PacketElement.DATA_LEN);
        return len;
    }

    /**
     * 读取 4字节Integer
     *
     * @param radix 进制
     * @return Integer
     * @throws IOException 读写异常
     */
    public int readInt32(int radix) throws IOException {
        char[] intChars = new char[4];
        count = reader.read(intChars);
        if (count != 4) {
            return -1;
        }
        return Integer.parseInt(new String(intChars), radix);
    }

    /**
     * 读取 数据段
     *
     * @return chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA
     */
    public char[] readData(int segmentLen) throws T212FormatException, IOException {
        char[] segment = new char[segmentLen];
        count = reader.read(segment);
        VerifyUtil.verifyLen(count, segmentLen, PacketElement.DATA);
        return segment;
    }

    /**
     * 读取 DATA_CRC 校验
     *
     * @return header chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA_CRC
     */
    public char[] readCrc() throws T212FormatException, IOException {
        char[] crc = new char[4];
        count = reader.read(crc);
        VerifyUtil.verifyLen(count, crc.length, PacketElement.DATA_CRC);
        return crc;
    }

    /**
     * 读取 数据段长度
     *
     * @return chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA_LEN
     */
    public char[] readFooter() throws T212FormatException, IOException {
        char[] footer = new char[2];
        count = reader.read(footer);
        VerifyUtil.verifyLen(count, 2, PacketElement.FOOTER);
        if (ParserFeature.FOOTER_CONSTANT.enabledIn(parserFeature)) {
            VerifyUtil.verifyChar(footer, FOOTER, PacketElement.FOOTER);
        }
        return footer;
    }

    /**
     * 读取 包尾
     *
     * @return chars
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#FOOTER
     */
    public char[] readDataAndCrc(int dataLen) throws IOException, T212FormatException {
        reader.mark(-0);
        char[] data = new char[dataLen];
        count = reader.read(data);
        VerifyUtil.verifyLen(count, dataLen, PacketElement.DATA);

        int crc = readInt32(16);
        if (crc != -1 &&
                crc16Checkout(data, dataLen) == crc) {
            return data;
        }
        reader.reset();
        return null;
    }


    @Override
    public void configured(Configurator<T212Parser> configurator) {
        configurator.config(this);
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * CRC校验
     *
     * @param msg    消息
     * @param length 长度
     * @return DATA_CRC 校验码
     */
    public static int crc16Checkout(char[] msg, int length) {
        int i, j, crc_reg, check;

        crc_reg = 0xFFFF;
        for (i = 0; i < length; i++) {
            crc_reg = (crc_reg >> 8) ^ msg[i];
            for (j = 0; j < 8; j++) {
                check = crc_reg & 0x0001;
                crc_reg >>= 1;
                if (check == 0x0001) {
                    crc_reg ^= 0xA001;
                }
            }
        }
        return crc_reg;
    }
}
