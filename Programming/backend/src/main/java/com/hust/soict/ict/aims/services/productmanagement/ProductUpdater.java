package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.ArrayList;
import java.util.List;

abstract class ProductUpdater<P extends Product, UR extends UpdateProductRequest, UC extends Product.UpdateCommand<?>> {
    protected abstract Class<UR> updateRequestType();
    protected abstract List<UC> update(UR request);

    protected P update(P product, UR request) {
        List<Product.UpdateCommand<?>> commands = new ArrayList<>();

        request.getTitle().ifDefined(title -> commands.add(new Product.UpdateCommand.Title(title)));
        request.getCategory().ifDefined(category -> commands.add(new Product.UpdateCommand.Category(category)));
        request.getDescription().ifDefined(description -> commands.add(new Product.UpdateCommand.Description(description)));
        request.getImageURL().ifDefined(imageURL -> commands.add(new Product.UpdateCommand.ImageUrl(imageURL)));
        request.getHeight().ifDefined(height -> commands.add(new Product.UpdateCommand.Height(height)));
        request.getWidth().ifDefined(width -> commands.add(new Product.UpdateCommand.Width(width)));
        request.getLength().ifDefined(length -> commands.add(new Product.UpdateCommand.Length(length)));
        request.getWeight().ifDefined(weight -> commands.add(new Product.UpdateCommand.Weight(weight)));

        commands.addAll(update(request));

        commands.forEach(product::update);
        return product;
    }
}
