package me.hao0.wechat.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.hao0.wechat.model.data.article.ArticleShareScene;
import java.io.IOException;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleShareSceneDeserializer extends JsonDeserializer<ArticleShareScene> {

    @Override
    public ArticleShareScene deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ArticleShareScene.from(parser.getIntValue());
    }
}
