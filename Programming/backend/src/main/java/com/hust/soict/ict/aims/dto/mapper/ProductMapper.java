package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.Product;

public interface ProductMapper {
    <P extends Product, D extends ProductDetail> D toProductDetail(P product);
    <P extends Product> ProductSummary toProductSummary(P product);
}
