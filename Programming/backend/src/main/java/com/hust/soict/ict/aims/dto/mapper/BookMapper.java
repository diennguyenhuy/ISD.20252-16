package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.BookDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

@Component
class BookMapper extends PrintableProductMapper<Book, BookDetail> {
    BookMapper() {
        super(BookDetail::new);
    }

    @Override
    public void map(Book source, BookDetail target) {
        target.setAuthors(source.getAuthors());
        target.setCoverType(source.getCoverType().name());
        target.setNumberOfPages(source.getNumberOfPages());
        target.setGenre(source.getGenre());
    }

    @Override
    public Class<Book> getSourceClass() {
        return Book.class;
    }
}
