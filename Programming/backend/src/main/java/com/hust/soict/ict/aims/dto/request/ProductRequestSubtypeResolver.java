package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.models.entities.product.Book;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.jsontype.NamedType;
import tools.jackson.databind.module.SimpleModule;

@Configuration
class ProductRequestSubtypeResolver {
    @Bean
    JacksonModule productSubtypeModule() {
        SimpleModule module = new SimpleModule();

        module.registerSubtypes(
                new NamedType(UpdateBookRequest.class, Book.class.getSimpleName()),
                new NamedType(UpdateNewspaperRequest.class, Newspaper.class.getSimpleName()),
                new NamedType(UpdateCDRequest.class, CD.class.getSimpleName()),
                new NamedType(UpdateDVDRequest.class, DVD.class.getSimpleName())
        ).registerSubtypes(
                new NamedType(CreateBookRequest.class, Book.class.getSimpleName()),
                new NamedType(CreateNewspaperRequest.class, Newspaper.class.getSimpleName()),
                new NamedType(CreateCDRequest.class, CD.class.getSimpleName()),
                new NamedType(CreateDVDRequest.class, DVD.class.getSimpleName())
        );

        return module;
    }
}
