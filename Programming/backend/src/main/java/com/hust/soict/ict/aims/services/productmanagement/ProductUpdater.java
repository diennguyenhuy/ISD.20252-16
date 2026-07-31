package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.ArrayList;
import java.util.List;

abstract class ProductUpdater<P extends Product, UR extends UpdateProductRequest, UC extends Product.UpdateCommand<?>> {
    private final Class<UR> updateRequestType;

    protected ProductUpdater(Class<UR> updateRequestType) {
        this.updateRequestType = updateRequestType;
    }

    final Class<UR> updateRequestType() {
        return updateRequestType;
    }

    protected abstract List<UC> productCommands(UR request);

    private List<Product.UpdateCommand<?>> commands(UR request) {
        List<Product.UpdateCommand<?>> commands = new ArrayList<>();

        request.getTitle().ifDefined(title -> commands.add(new Product.UpdateCommand.Title(title)));
        request.getCategory().ifDefined(category -> commands.add(new Product.UpdateCommand.Category(category)));
        request.getDescription().ifDefined(description -> commands.add(new Product.UpdateCommand.Description(description)));
        request.getImageURL().ifDefined(imageURL -> commands.add(new Product.UpdateCommand.ImageUrl(imageURL)));
        request.getHeight().ifDefined(height -> commands.add(new Product.UpdateCommand.Height(height)));
        request.getWidth().ifDefined(width -> commands.add(new Product.UpdateCommand.Width(width)));
        request.getLength().ifDefined(length -> commands.add(new Product.UpdateCommand.Length(length)));
        request.getWeight().ifDefined(weight -> commands.add(new Product.UpdateCommand.Weight(weight)));
        commands.addAll(productCommands(request));

        return commands;
    }

    protected final P update(P product, UR request) {
        commands(request).forEach(product::update);
        return product;
    }
}
