package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

interface ProductCreator<P extends Product, C extends CreateProductRequest> {
    Class<C> createRequestType();
    P createFrom(C createRequest);
}
