package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

class UpdateFieldRequestDeserializer extends JsonDeserializer<UpdateFieldRequest<?>> implements ContextualDeserializer {
    private final JsonDeserializer<?> valueDeserializer;

    UpdateFieldRequestDeserializer() {
        this.valueDeserializer = null;
    }

    private UpdateFieldRequestDeserializer(JsonDeserializer<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public UpdateFieldRequest<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.getCurrentToken() == JsonToken.VALUE_NULL) {
            return UpdateFieldRequest.defined(null);
        }

        Object value = valueDeserializer.deserialize(p, ctxt);

        return UpdateFieldRequest.defined(value);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JavaType wrapperType = property != null ? property.getType() : ctxt.getContextualType();

        JavaType containedType = wrapperType.containedType(0);

        if (containedType == null) {
            throw JsonMappingException.from(
                    ctxt,
                    "UpdateFieldRequest deserializer cannot find contained type. " +
                            "If you write UpdateFieldRequest, must specified its concrete generic type, e.g. UpdateFieldRequest<String>, not UpdateFieldRequest."
            );
        }

        JsonDeserializer<?> delegate = ctxt.findContextualValueDeserializer(containedType, property);

        return new UpdateFieldRequestDeserializer(delegate);
    }
}

@Configuration
class UpdateFieldRequestDeserializerConfiguration {
    @Bean
    Module fieldUpdateRequestModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(UpdateFieldRequest.class, new UpdateFieldRequestDeserializer());

        return module;
    }
}