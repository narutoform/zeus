package cn.chinotan.zeus.framework.config.jackson.serializer;

import cn.chinotan.zeus.framework.config.converter.StringToIntegerUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author xingcheng
 * @date 2018-11-08
 */
public class JacksonIntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String string = jsonParser.getText();
        return StringToIntegerUtil.convert(string);
    }
}
