package com.hust.soict.ict.aims.models.entities.product;

import java.util.List;

public interface DVDUpdateCommand<T> extends ProductUpdateCommand<T> {
    record Genre(String newValue) implements DVDUpdateCommand<String> {}
    record Director(String newValue) implements DVDUpdateCommand<String> {}
    record Runtime(Integer newValue) implements DVDUpdateCommand<Integer> {}
    record Studio(String newValue) implements DVDUpdateCommand<String> {}
    record Language(String newValue) implements DVDUpdateCommand<String> {}
    record Subtitles(List<String> newValue) implements DVDUpdateCommand<List<String>> {}
}
