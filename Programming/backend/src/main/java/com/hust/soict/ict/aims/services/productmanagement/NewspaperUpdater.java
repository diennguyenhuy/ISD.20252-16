package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateNewspaperRequest;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class NewspaperUpdater extends PrintableProductUpdater<Newspaper, UpdateNewspaperRequest, Newspaper.UpdateCommand<?>> {

    NewspaperUpdater() {
        super(UpdateNewspaperRequest.class, request -> {
            List<Newspaper.UpdateCommand<?>> commands = new ArrayList<>();

            request.getEditorInChief().ifDefined(editorInChief -> commands.add(new Newspaper.UpdateCommand.EditorInChief(editorInChief)));
            request.getPublicationFrequency().ifDefined(pf -> commands.add(new Newspaper.UpdateCommand.PublicationFrequency(pf)));
            request.getSections().ifDefined(sections -> commands.add(new Newspaper.UpdateCommand.Sections(sections)));

            return commands;
        });
    }
}
