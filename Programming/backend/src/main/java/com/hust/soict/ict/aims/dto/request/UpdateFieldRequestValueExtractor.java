package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@UnwrapByDefault
class UpdateFieldRequestValueExtractor implements ValueExtractor<UpdateFieldRequest<@ExtractedValue ?>> {
    @Override
    public void extractValues(UpdateFieldRequest<?> originalValue, ValueReceiver receiver) {
        if (originalValue != null && originalValue.isDefined()) {
            receiver.value(null, originalValue.get());
        }
    }
}

@Configuration
class UpdateFieldRequestValueExtractorConfiguration {
    @Bean
    LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

        validator.setConfigurationInitializer(c -> c.addValueExtractor(new UpdateFieldRequestValueExtractor()));

        return validator;
    }
}