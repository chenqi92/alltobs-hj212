package com.alltobs.hj212.deser;

import com.alltobs.hj212.config.SegmentParser;
import com.alltobs.hj212.config.SegmentToken;
import com.alltobs.hj212.exception.SegmentFormatException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 功能:
 *
 * @author chenQi
 */
public class MapSegmentDeserializer
        implements SegmentDeserializer<Map<String,Object>> {


    protected final SegmentDeserializer<?> _valueDeserializer;
    protected final SegmentDeserializer<String> _stringValueDeserializer;

    public MapSegmentDeserializer() {
        _valueDeserializer = new MapValueSegmentDeserializer(this);
        _stringValueDeserializer = new StringSegmentDeserializer();
    }

    public MapSegmentDeserializer(SegmentDeserializer<Object> valueDeserializer) {
        _valueDeserializer = valueDeserializer;
        _stringValueDeserializer = new StringSegmentDeserializer();
    }


    @Override
    public Map<String, Object> deserialize(SegmentParser parser) throws IOException, SegmentFormatException {
        if(parser.currentToken() == null){
            parser.initToken();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        readMap(parser,result);
        return result;
    }


    private void readMap(SegmentParser parser, Map<String, Object> result) throws IOException, SegmentFormatException {
        String key = parser.readKey();
        for (; key != null; key = parser.readKey()) {
            SegmentToken token = parser.currentToken();
            Object value = null;

            switch (token){
                case NOT_AVAILABLE:
                    //end
                    return;
                case END_PART_KEY:
                    //NOT possible
                    assert false;
                case END_SUB_ENTRY:
                case END_ENTRY:
                case END_OBJECT_VALUE:
                    //NULL value
                    break;
                case END_KEY:
                case START_OBJECT_VALUE:
                    //Normal value
                    value = _valueDeserializer.deserialize(parser);
                    break;
            }
            result.put(key, value);
        }
    }

}
