package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

import java.util.ArrayList;
import java.util.List;

abstract class PrintableProductUpdater<P extends PrintableProduct, UR extends UpdatePrintableProductRequest, UC extends PrintableProduct.UpdateCommand<?>> extends ProductUpdater<P, UR, UC> {

    protected PrintableProductUpdater(Class<UR> updateRequestType) {
        super(updateRequestType);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<UC> productCommands(UR request) {
        List<PrintableProduct.UpdateCommand<?>> commands = new ArrayList<>();

        request.getPublisher().ifDefined(publisher -> commands.add(new PrintableProduct.UpdateCommand.Publisher(publisher)));
        request.getLanguage().ifDefined(language -> commands.add(new PrintableProduct.UpdateCommand.Language(language)));
        commands.addAll(printableProductCommands(request));

        return (List<UC>) commands;
    }

    protected abstract List<UC> printableProductCommands(UR request);
}
