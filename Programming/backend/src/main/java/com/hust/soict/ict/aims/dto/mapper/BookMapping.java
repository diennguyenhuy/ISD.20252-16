package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.BookDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class BookMapping implements PrintableProductMapping<Book, BookDetail> {
    @Override
    public Class<Book> getProductClass() {
        return Book.class;
    }

    @Override
    public BookDetail map(Book product) {
        if (product == null) {
            return null;
        }

        BookDetail detail = new BookDetail();
        mapCommonFields(detail, product);
        detail.setAuthors(product.getAuthors());
        detail.setCoverType(product.getCoverType().name());
        detail.setNumberOfPages(product.getNumberOfPages());
        detail.setGenre(product.getGenre());

        return detail;
    }

    @Override
    public List<String> mapCreators(Book product) {
        return product.getAuthors();
    }

}
