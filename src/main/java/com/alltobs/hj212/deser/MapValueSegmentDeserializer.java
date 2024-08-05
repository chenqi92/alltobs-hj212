package com.alltobs.hj212.deser;

import com.alltobs.hj212.config.SegmentParser;
import com.alltobs.hj212.config.SegmentToken;
import com.alltobs.hj212.exception.SegmentFormatException;

import java.io.IOException;

/**
 * 功能:
 *
 * @author chenQi
 */
public class MapValueSegmentDeserializer
        implements SegmentDeserializer<Object> {

    protected final SegmentDeserializer<?> _valueDeserializer;

    public MapValueSegmentDeserializer(SegmentDeserializer<?> _valueDeserializer) {
        this._valueDeserializer = _valueDeserializer;
    }

    @Override
    public Object deserialize(SegmentParser parser) throws IOException, SegmentFormatException {
        Object result = null;
        SegmentToken token = parser.currentToken();
        switch (token) {
            case NOT_AVAILABLE:
            case NULL_VALUE:
                return null;
            case END_KEY:
                result = parser.readValue();
                break;
            case START_OBJECT_VALUE:
                result = _valueDeserializer.deserialize(parser);
                break;
        }

        return result;
    }
}
