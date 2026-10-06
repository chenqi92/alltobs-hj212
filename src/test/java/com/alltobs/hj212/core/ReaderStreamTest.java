package com.alltobs.hj212.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReaderStreamTest {

    @Test
    void unmatchedEndOfInputIsNotPushedBackAsACharacter() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader(""));
        ReaderStream<?> stream = ReaderStream.of(reader).next().when('x').skip().done();

        assertTrue(stream.match().isEmpty());
        assertEquals(-1, reader.read());
    }

    @Test
    void stopsScanningAtUnmatchedEndOfInput() throws IOException {
        Reader reader = new Reader() {
            private int reads;

            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                if (++reads > 8) {
                    throw new IOException("Repeated reads past end of input");
                }
                return -1;
            }

            @Override
            public void close() {
            }
        };
        ReaderStream<?> stream = ReaderStream.of(reader).next().when('x').skip().done();

        assertEquals(0, stream.read());
    }

    @Test
    void doesNotAppendSyntheticCharactersPastEndOfInput() throws IOException {
        ReaderStream<?> stream = ReaderStream.of(new StringReader("a"))
                .next().when('x').skip().done();
        CharBuffer buffer = CharBuffer.allocate(4);

        assertEquals(1, stream.read(buffer));
        buffer.flip();
        assertEquals("a", buffer.toString());
    }

    @Test
    void preservesExplicitEndOfInputHandlers() throws IOException {
        AtomicBoolean ended = new AtomicBoolean();
        ReaderStream<?> stream = ReaderStream.of(new StringReader(""))
                .next().when(c -> c == (char) -1).then(() -> ended.set(true)).done();

        assertTrue(stream.match().isPresent());
        assertTrue(ended.get());
    }

    @Test
    void rollsBackOnlyCharactersActuallyRead() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader("a"), 2);
        ReaderStream<?> stream = ReaderStream.of(reader)
                .next(2).when('&', '&').then(() -> {}).done();

        assertTrue(stream.match().isEmpty());
        assertEquals('a', reader.read());
        assertEquals(-1, reader.read());
    }

    @Test
    void cannotMatchZeroPaddingOnIncompleteInput() throws IOException {
        ReaderStream<?> stream = ReaderStream.of(new StringReader("a"))
                .next(2).when('a', '\0').then(() -> {}).done();

        assertTrue(stream.match().isEmpty());
    }

    @Test
    void matchesCharactersAcrossShortReads() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader("&&z") {
            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                return super.read(buffer, offset, Math.min(length, 1));
            }
        }, 2);
        ReaderStream<?> stream = ReaderStream.of(reader)
                .next(2).when('&', '&').then(() -> {}).done();

        assertTrue(stream.match().isPresent());
        assertEquals('z', reader.read());
    }
}
