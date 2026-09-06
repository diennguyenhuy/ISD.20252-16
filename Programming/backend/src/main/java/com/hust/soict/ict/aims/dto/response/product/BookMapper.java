package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class BookMapper extends PrintableProductMapper<Book, BookDetail> {
    BookMapper() {
        super(BookDetail::new, Book.class);
    }

    @Override
    protected void mapPrintableProduct(Book product, BookDetail productDetail) {
        productDetail.setAuthors(product.getAuthors());
        productDetail.setCoverType(product.getCoverType().name());
        product.getNumberOfPages().ifPresent(productDetail::setNumberOfPages);
        product.getGenre().ifPresent(productDetail::setGenre);
    }

    @Override
    protected List<String> creators(Book product) {
        return product.getAuthors();
    }
}
