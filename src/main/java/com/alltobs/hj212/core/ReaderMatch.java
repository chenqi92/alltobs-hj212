package com.alltobs.hj212.core;

import com.alltobs.hj212.lambda.RunnableWithThrowable;
import com.alltobs.hj212.lambda.SupplierWithThrowable;

import java.io.IOException;
import java.util.Optional;

/**
 * 功能: 字符读取匹配
 *
 * @author chenQi
 */
public interface ReaderMatch<THIS extends ReaderMatch, ParentStream extends ReaderStream, T> {


    /**
     * 字符读取流
     *
     * @return ParentStream
     */
    ParentStream done();

    /**
     * 匹配
     *
     * @return 匹配到
     * @throws IOException
     */
    Optional<T> match() throws IOException;


    static SupplierWithThrowable<Optional<Object>, IOException> alwaysReturnPresent() {
        return () -> Optional.of(true);
    }

    static SupplierWithThrowable<Optional<Object>, IOException> alwaysReturnNotPresent() {
        return Optional::empty;
    }

    static SupplierWithThrowable<Optional<Object>, IOException> alwaysReturnPresentRunable(RunnableWithThrowable<IOException> runnable) {
        return () -> {
            runnable.run();
            return Optional.of(true);
        };
    }

    static SupplierWithThrowable<Optional<Object>, IOException> alwaysReturnNotPresentRunable(RunnableWithThrowable<IOException> runnable) {
        return () -> {
            runnable.run();
            return Optional.empty();
        };
    }
}
