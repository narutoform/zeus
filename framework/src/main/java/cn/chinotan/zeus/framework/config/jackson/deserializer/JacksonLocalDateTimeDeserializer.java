package cn.chinotan.zeus.framework.config.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import cn.chinotan.zeus.config.constant.DatePattern;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *     Jackson LocaDateTime反序列化器
 * </p>
 * @author xingcheng
 * @date 2018-11-08
 */
public class JacksonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String string = jp.getText();
        if (StringUtils.isBlank(string)){
            return null;
        }
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS));
    }

}
