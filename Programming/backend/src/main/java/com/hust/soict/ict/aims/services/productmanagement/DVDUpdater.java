package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class DVDUpdater extends ProductUpdater<DVD, UpdateDVDRequest, DVD.UpdateCommand<?>> {
    @Override
    protected Class<UpdateDVDRequest> updateRequestType() {
        return UpdateDVDRequest.class;
    }

    @Override
    protected List<DVD.UpdateCommand<?>> update(UpdateDVDRequest request) {
        List<DVD.UpdateCommand<?>> commands = new ArrayList<>();

        request.getGenre().ifDefined(genre -> commands.add(new DVD.UpdateCommand.Genre(genre)));
        request.getDirector().ifDefined(director -> commands.add(new DVD.UpdateCommand.Director(director)));
        request.getRuntime().ifDefined(runtime -> commands.add(new DVD.UpdateCommand.Runtime(runtime)));
        request.getStudio().ifDefined(studio -> commands.add(new DVD.UpdateCommand.Studio(studio)));
        request.getLanguage().ifDefined(language -> commands.add(new DVD.UpdateCommand.Language(language)));
        request.getSubtitles().ifDefined(subtitles -> commands.add(new DVD.UpdateCommand.Subtitles(subtitles)));

        return commands;
    }
}
