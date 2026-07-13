package com.hust.soict.ict.aims.models.entities.product;

import java.util.List;

public interface NewspaperUpdateCommand<T> extends PrintableProductUpdateCommand<T> {
    record EditorInChief(String newValue) implements NewspaperUpdateCommand<String> {}
    record PublicationFrequency(String newValue) implements NewspaperUpdateCommand<String> {}
    record Sections(List<String> newValue) implements NewspaperUpdateCommand<List<String>> {}
}
