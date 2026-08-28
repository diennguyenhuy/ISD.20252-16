package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
class BookUpdater extends PrintableProductUpdater<Book, UpdateBookRequest> {

    BookUpdater() {
        super(UpdateBookRequest.class);
    }

    @Override
    protected void updatePrintableProduct(Book product, UpdateBookRequest request) {
        request.getAuthors().ifDefined(product::setAuthors);
        request.getNumberOfPages().ifDefined(product::setNumberOfPages);
        request.getGenre().ifDefined(product::setGenre);
    }
}
