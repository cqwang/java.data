package cqwang.data.serializer.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * 去除高精度数值末尾的0
 */
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        if (Objects.isNull(jsonNode)) {
            return null;
        }

        var value = jsonNode.asDouble();
        var str = MessageFormat.format("{0,number,#.##}", BigDecimal.valueOf(value));
        return new BigDecimal(str);
    }
}

