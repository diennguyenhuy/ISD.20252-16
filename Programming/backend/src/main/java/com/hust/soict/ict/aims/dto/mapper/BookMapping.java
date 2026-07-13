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
    public List<String> mapCreators(Book product) {
        return product.getAuthors();
    }

    @Override
    public BookDetail newProductDetail() {
        return new BookDetail();
    }

    @Override
    public void map(BookDetail productDetail, Book product) {
        productDetail.setAuthors(product.getAuthors());
        productDetail.setCoverType(product.getCoverType().name());
        productDetail.setNumberOfPages(product.getNumberOfPages());
        productDetail.setGenre(product.getGenre());
    }
}
