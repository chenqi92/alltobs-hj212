package com.alltobs.hj212.format;

import com.alltobs.hj212.core.Configurator;
import com.alltobs.hj212.core.Configured;
import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.model.verify.PacketElement;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

/**
 * 功能:
 *
 * @author chenQi
 */
public class T212Generator implements Configured<T212Generator>, Closeable {

    public static char[] HEADER = new char[]{'#', '#'};
    public static char[] FOOTER = new char[]{'\r', '\n'};

    protected Writer writer;
    /**
     * not use
     */
    private int generatorFeature;

    public T212Generator(Writer writer) {
        this.writer = writer;
    }

    /**
     * 设置生成特性
     *
     * @param generatorFeature 生成特性
     */
    public void setGeneratorFeature(int generatorFeature) {
        this.generatorFeature = generatorFeature;
    }

    /**
     * 写入 包头
     *
     * @return count always 2
     * @throws IOException 读写异常
     * @see PacketElement#HEADER
     */
    public int writeHeader() throws IOException {
        writer.write(HEADER);
        return 2;
    }

    /**
     * 写入 数据段长度
     *
     * @param len chars
     * @return length always 4
     * @throws IOException         读写异常
     * @throws T212FormatException 212编码转换异常
     * @see PacketElement#DATA_LEN
     */
    public int writeDataLen(char[] len) throws IOException, T212FormatException {
        VerifyUtil.verifyLen(len.length, 4, PacketElement.DATA_LEN);
        writer.write(len);
        return 4;
    }

    /**
     * 写入 4字节Integer
     *
     * @param i integer
     * @return length always 4
     * @throws IOException 读写异常
     */
    public int writeHexInt32(int i) throws IOException {
        char[] intChars = String.format("%04x", i).toCharArray();
        writer.write(intChars);
        return intChars.length;
    }

    /**
     * 读取 数据段
     *
     * @param data data chars
     * @return data length
     * @throws IOException 读写异常
     * @see PacketElement#DATA
     */
    public int writeData(char[] data) throws IOException {
        writer.write(data);
        return data.length;
    }

    /**
     * 写入 DATA_CRC 校验
     *
     * @param crc crc chars
     * @return length always 4
     * @throws T212FormatException 212编码转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA_CRC
     */
    public int writeCrc(char[] crc) throws IOException, T212FormatException {
        VerifyUtil.verifyLen(crc.length, 4, PacketElement.DATA_LEN);
        writer.write(crc);
        return crc.length;
    }

    /**
     * 写入 数据段长度+CRC校验
     *
     * @param data data chars
     * @return data length with 8 chars(4 chars for data_len and 4 chars for data_crc)
     * @throws T212FormatException 212协议转换异常
     * @throws IOException         读写异常
     * @see PacketElement#DATA_LEN
     */
    public int writeDataAndLenAndCrc(char[] data) throws IOException, T212FormatException {
        int dataLen = data.length;
        char[] len = String.format("%04d", dataLen).toCharArray();
        writer.write(len);
        VerifyUtil.verifyLen(len.length, 4, PacketElement.DATA_CRC);

        writer.write(data);

        int crc = T212Parser.crc16Checkout(data, dataLen);
        int crcLen = writeHexInt32(crc);
        VerifyUtil.verifyLen(crcLen, 4, PacketElement.DATA_CRC);

        return len.length + data.length + crcLen;
    }

    /**
     * 写入 包尾
     *
     * @return length always 2
     * @throws IOException 读写异常
     * @see PacketElement#FOOTER
     */
    public int writeFooter() throws IOException {
        writer.write(FOOTER);
        return 2;
    }


    @Override
    public void configured(Configurator<T212Generator> configurator) {
        configurator.config(this);
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
