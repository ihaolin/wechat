package me.hao0.wechat.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.hao0.wechat.model.data.article.ArticleSource;
import java.io.IOException;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleSourceDeserializer extends JsonDeserializer<ArticleSource> {

    @Override
    public ArticleSource deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ArticleSource.from(parser.getIntValue());
    }
}
