package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
class BookCreator implements ProductCreator<Book, CreateBookRequest>, PrintableProductCommonCreator<CreateBookRequest, Book.Builder> {
    @Override
    public Class<CreateBookRequest> createRequestType() {
        return CreateBookRequest.class;
    }

    @Override
    public Book createFrom(CreateBookRequest createRequest) {
        return buildCommonFields(new Book.Builder(), createRequest)
                .authors(createRequest.getAuthors())
                .coverType(createRequest.getCoverType())
                .numberOfPages(createRequest.getNumberOfPages())
                .genre(createRequest.getGenre())
                .build();
    }
}
