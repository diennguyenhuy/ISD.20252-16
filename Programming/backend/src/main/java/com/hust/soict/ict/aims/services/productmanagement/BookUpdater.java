package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class BookUpdater extends PrintableProductUpdater<Book, UpdateBookRequest, Book.UpdateCommand<?>> {

    @Override
    protected Class<UpdateBookRequest> updateRequestType() {
        return UpdateBookRequest.class;
    }

    @Override
    protected List<Book.UpdateCommand<?>> update(UpdateBookRequest request) {
        List<Book.UpdateCommand<?>> commands = new ArrayList<>();

        request.getAuthors().ifDefined(authors -> commands.add(new Book.UpdateCommand.Authors(authors)));
        request.getNumberOfPages().ifDefined(numbers -> commands.add(new Book.UpdateCommand.NumberOfPages(numbers)));
        request.getGenre().ifDefined(genres -> commands.add(new Book.UpdateCommand.Genre(genres)));

        return commands;
    }
}
