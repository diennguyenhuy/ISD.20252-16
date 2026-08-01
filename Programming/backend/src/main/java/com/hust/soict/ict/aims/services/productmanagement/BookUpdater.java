package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateBookRequest;
import com.hust.soict.ict.aims.models.entities.product.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class BookUpdater extends PrintableProductUpdater<Book, UpdateBookRequest, Book.UpdateCommand<?>> {

    BookUpdater() {
        super(UpdateBookRequest.class, request -> {
            List<Book.UpdateCommand<?>> commands = new ArrayList<>();

            request.getAuthors().ifDefined(authors -> commands.add(new Book.UpdateCommand.Authors(authors)));
            request.getNumberOfPages().ifDefined(numbers -> commands.add(new Book.UpdateCommand.NumberOfPages(numbers)));
            request.getGenre().ifDefined(genres -> commands.add(new Book.UpdateCommand.Genre(genres)));

            return commands;
        });
    }
}
