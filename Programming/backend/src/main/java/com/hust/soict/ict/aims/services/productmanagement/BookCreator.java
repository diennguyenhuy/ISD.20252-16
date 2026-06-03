package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
public class BookCreator implements ProductCreator<Book, CreateBookRequest> {
    @Override
    public Class<CreateBookRequest> createRequestType() {
        return CreateBookRequest.class;
    }

    @Override
    public Book createFrom(CreateBookRequest createRequest) {
        return populateCommonFields(new Book.Builder(), createRequest)
                .publisher(createRequest.getPublisher())
                .publicationDate(createRequest.getPublicationDate())
                .language(createRequest.getLanguage())
                .authors(createRequest.getAuthors())
                .coverType(createRequest.getCoverType())
                .numberOfPages(createRequest.getNumberOfPages())
                .genre(createRequest.getGenre())
                .build();
    }
}
