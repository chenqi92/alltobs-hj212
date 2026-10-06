package com.alltobs.hj212.format;

import com.alltobs.hj212.exception.T212FormatException;
import com.alltobs.hj212.feature.ParserFeature;
import com.alltobs.hj212.model.verify.PacketElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class T212ParserTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void readsCompletePacketAcrossShortReads(int chunkSize) throws Exception {
        String data = "ST=32;CN=2011;PW=123456;MN=NJGDKYYC202101q0001w0001;CP=&&DataTime=20210305003817&&";
        StringReader reader = new StringReader(packet(data)) {
            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                return super.read(buffer, offset, Math.min(length, chunkSize));
            }
        };
        try (T212Parser parser = new T212Parser(reader)) {
            parser.setParserFeature(ParserFeature.HEADER_CONSTANT.getMask()
                    | ParserFeature.FOOTER_CONSTANT.getMask());

            assertArrayEquals(new char[]{'#', '#'}, parser.readHeader());
            assertEquals(data.length(), Integer.parseInt(new String(parser.readDataLen())));
            assertEquals(data, new String(parser.readData(data.length())));
            VerifyUtil.verifyCrc(data.toCharArray(), parser.readCrc(), PacketElement.DATA_CRC);
            assertArrayEquals(new char[]{'\r', '\n'}, parser.readFooter());
        }
    }

    @Test
    void rejectsTruncatedFields() throws Exception {
        try (T212Parser parser = new T212Parser(new StringReader("#"))) {
            assertThrows(T212FormatException.class, parser::readHeader);
        }
        try (T212Parser parser = new T212Parser(new StringReader("abc"))) {
            assertThrows(T212FormatException.class, () -> parser.readData(4));
        }
        try (T212Parser parser = new T212Parser(new StringReader("12"))) {
            assertEquals(-1, parser.readInt32(16));
        }
    }

    @Test
    void readsEmptyDataWithoutConsumingCrc() throws Exception {
        try (T212Parser parser = new T212Parser(new StringReader("ffff"))) {
            assertArrayEquals(new char[0], parser.readData(0));
            assertEquals(65535, parser.readInt32(16));
        }
    }

    @Test
    void restoresPositionAfterCrcMismatchOnBufferedInput() throws Exception {
        try (T212Parser parser = new T212Parser(new BufferedReader(new StringReader("abc0000"), 2))) {
            assertNull(parser.readDataAndCrc(3));
            assertEquals("abc", new String(parser.readData(3)));
        }
    }

    private String packet(String data) throws Exception {
        StringWriter writer = new StringWriter();
        try (T212Generator generator = new T212Generator(writer)) {
            generator.writeHeader();
            generator.writeDataAndLenAndCrc(data.toCharArray());
            generator.writeFooter();
        }
        return writer.toString();
    }
}
