package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class BookSummaryMapper extends ProductSummaryMapper<Book> {
    @Override
    protected List<String> mapCreators(Book product) {
        return product.getAuthors();
    }

    @Override
    public Class<Book> getSourceClass() {
        return Book.class;
    }
}
