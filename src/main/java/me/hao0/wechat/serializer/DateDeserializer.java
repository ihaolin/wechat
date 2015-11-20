package me.hao0.wechat.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {
 
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
        return new Date(parser.getIntValue() * 1000L);
    }
}