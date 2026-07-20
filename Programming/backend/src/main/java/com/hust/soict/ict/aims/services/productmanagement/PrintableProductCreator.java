package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

import java.util.function.Supplier;

abstract class PrintableProductCreator<
        P extends PrintableProduct,
        C extends CreatePrintableProductRequest,
        B extends PrintableProduct.Builder<B>
        > extends ProductCreator<P, C, B> {

    protected PrintableProductCreator(Supplier<B> builderSupplier) {
        super(builderSupplier);
    }

    @Override
    protected B builder(C request) {
        return super.builder(request)
                .publisher(request.getPublisher())
                .publicationDate(request.getPublicationDate())
                .language(request.getLanguage());
    }
}
