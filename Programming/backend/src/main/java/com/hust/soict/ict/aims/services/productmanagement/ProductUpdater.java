package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

interface ProductUpdater<P extends Product, U extends UpdateProductRequest> {
    Class<U> updateRequestType();
    P updateFrom(P existingProduct, U updateRequest);
}
