package com.alltobs.hj212.core;

import java.io.IOException;
import java.util.Optional;

/**
 * 功能:
 *
 * @author chenQi
 */
public class NoneReadMatch implements ReaderMatch {
    @Override
    public Optional match() throws IOException {
        return Optional.empty();
    }

    @Override
    public ReaderStream done() {
        return null;
    }
}
