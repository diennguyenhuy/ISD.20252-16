package com.hust.soict.ict.aims.models.entities.product;

import java.util.List;

public interface CDUpdateCommand<T> extends ProductUpdateCommand<T> {
    record Genre(String newValue) implements CDUpdateCommand<String> {}
    record Artists(List<String> newValue) implements CDUpdateCommand<List<String>> {}
    record RecordLabel(String newValue) implements CDUpdateCommand<String> {}
    record Tracks(List<Track> newValue) implements CDUpdateCommand<List<Track>> {}
}
