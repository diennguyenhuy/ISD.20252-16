package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.ArrayList;
import java.util.List;

abstract class PrintableProductUpdater<P extends PrintableProduct, UR extends UpdatePrintableProductRequest, UC extends PrintableProduct.UpdateCommand<?>> extends ProductUpdater<P, UR, UC> {

    @Override
    protected P update(P product, UR request) {
        product = super.update(product, request);

        List<Product.UpdateCommand<?>> commands = new ArrayList<>();

        request.getPublisher().ifDefined(publisher -> commands.add(new PrintableProduct.UpdateCommand.Publisher(publisher)));
        request.getLanguage().ifDefined(language -> commands.add(new PrintableProduct.UpdateCommand.Language(language)));

        commands.forEach(product::update);

        return product;
    }
}
