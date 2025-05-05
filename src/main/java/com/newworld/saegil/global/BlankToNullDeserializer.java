package com.newworld.saegil.global;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class BlankToNullDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private final JavaType valueType;

    public BlankToNullDeserializer() {
        this.valueType = null;
    }

    public BlankToNullDeserializer(JavaType valueType) {
        this.valueType = valueType;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (text == null || text.isBlank()) {
                return null;
            }
        }

        final ObjectMapper mapper = (ObjectMapper) p.getCodec();

        return mapper.readValue(p, valueType);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        final JavaType type = property != null ? property.getType() : ctxt.constructType(Object.class);

        return new BlankToNullDeserializer(type);
    }
}
