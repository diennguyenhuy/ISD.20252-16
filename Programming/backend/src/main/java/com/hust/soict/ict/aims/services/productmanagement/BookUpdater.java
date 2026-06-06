package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
public class BookUpdater implements ProductUpdater<Book, UpdateBookRequest> {

    @Override
    public Class<UpdateBookRequest> updateRequestType() {
        return UpdateBookRequest.class;
    }

    @Override
    public Book updateFrom(Book existingProduct, UpdateBookRequest updateRequest) {
        return populateCommonFields(existingProduct.toBuilder(), updateRequest)
                .publisher(updateRequest.getPublisher())
                .language(updateRequest.getLanguage())
                .authors(updateRequest.getAuthors())
                .numberOfPages(updateRequest.getNumberOfPages())
                .genre(updateRequest.getGenre())
                .build();
    }
}
