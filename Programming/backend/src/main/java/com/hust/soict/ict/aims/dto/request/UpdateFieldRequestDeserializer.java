package com.hust.soict.ict.aims.dto.request;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.*;
import tools.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class UpdateFieldRequestDeserializer extends ValueDeserializer<UpdateFieldRequest<?>> {
    private final ValueDeserializer<?> valueDeserializer;

    UpdateFieldRequestDeserializer() {
        this.valueDeserializer = null;
    }

    @Override
    public UpdateFieldRequest<?> deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return UpdateFieldRequest.defined(null);
        }

        Object value = valueDeserializer.deserialize(p, ctxt);

        return UpdateFieldRequest.defined(value);
    }

    private UpdateFieldRequestDeserializer(ValueDeserializer<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType wrapperType = property != null ? property.getType() : ctxt.getContextualType();

        JavaType containedType = wrapperType.containedType(0);

        if (containedType == null) {
            return ctxt.reportBadDefinition(
                    wrapperType,
                    "UpdateFieldRequest deserializer cannot find contained type. " +
                            "If you write UpdateFieldRequest, must specified its concrete generic type, e.g. UpdateFieldRequest<String>, not UpdateFieldRequest."
            );
        }

        ValueDeserializer<?> delegate = ctxt.findContextualValueDeserializer(containedType, property);

        return new UpdateFieldRequestDeserializer(delegate);
    }
}

@Configuration
class UpdateFieldRequestDeserializerConfiguration {
    @Bean
    JacksonModule fieldUpdateRequestModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(UpdateFieldRequest.class, new UpdateFieldRequestDeserializer());

        return module;
    }
}