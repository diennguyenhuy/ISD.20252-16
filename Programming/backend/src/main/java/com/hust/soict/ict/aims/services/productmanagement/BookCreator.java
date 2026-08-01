package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
class BookCreator extends PrintableProductCreator<Book, CreateBookRequest, Book.Builder> {

    BookCreator() {
        super(CreateBookRequest.class, Book.Builder::new);
    }

    @Override
    public Book createFrom(CreateBookRequest createRequest) {
        return builder(createRequest)
                .authors(createRequest.getAuthors())
                .coverType(createRequest.getCoverType())
                .numberOfPages(createRequest.getNumberOfPages())
                .genre(createRequest.getGenre())
                .build();
    }
}
