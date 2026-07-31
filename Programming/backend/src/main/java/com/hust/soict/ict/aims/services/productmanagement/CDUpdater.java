package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class CDUpdater extends ProductUpdater<CD, UpdateCDRequest, CD.UpdateCommand<?>> {

    CDUpdater() {
        super(UpdateCDRequest.class);
    }

    @Override
    protected List<CD.UpdateCommand<?>> productCommands(UpdateCDRequest request) {
        List<CD.UpdateCommand<?>> commands = new ArrayList<>();

        request.getGenre().ifDefined(genre -> commands.add(new CD.UpdateCommand.Genre(genre)));
        request.getArtists().ifDefined(artists -> commands.add(new CD.UpdateCommand.Artists(artists)));
        request.getRecordLabel().ifDefined(record -> commands.add(new CD.UpdateCommand.RecordLabel(record)));
        request.getTracks()
                .map(ts -> ts.stream()
                        .map(t -> new Track(t.title(), t.length()))
                        .toList()
                ).ifDefined(tracks -> commands.add(new CD.UpdateCommand.Tracks(tracks)));

        return commands;
    }
}
