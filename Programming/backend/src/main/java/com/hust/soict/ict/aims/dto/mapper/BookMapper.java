package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.BookDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
class BookMapper extends PrintableProductMapper<Book, BookDetail> {
    BookMapper() {
        super(Book.class, BookDetail::new);
    }

    @Override
    protected void mapPrintableProduct(Book source, BookDetail productDetail) {
        productDetail.setAuthors(source.getAuthors());
        productDetail.setCoverType(source.getCoverType().name());
        productDetail.setNumberOfPages(source.getNumberOfPages());
        productDetail.setGenre(source.getGenre());
    }
}
