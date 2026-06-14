package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;

public interface ProductMapping<P extends Product, D extends ProductDetail> {
    Class<P> getProductClass();
    D map(P product);
    List<String> mapCreators(P product);

    default void mapCommonFields(D productDetail, P product) {
        productDetail.setId(product.getId());
        productDetail.setTitle(product.getTitle());
        productDetail.setCategory(product.getCategory());
        productDetail.setDescription(product.getDescription());
        productDetail.setHeight(product.getHeight());
        productDetail.setWidth(product.getWidth());
        productDetail.setLength(product.getLength());
        productDetail.setWeight(product.getWeight());
        productDetail.setBarcode(product.getBarcode());
        productDetail.setOriginalValue(product.getOriginalValue());
        productDetail.setCurrentPrice(product.getCurrentPrice());
        productDetail.setStockQuantity(product.getStockQuantity());
        productDetail.setStatus(product.getStatus().name());
        productDetail.setImageURL(product.getImageURL());
        productDetail.setCreatedAt(product.getCreatedAt());
        productDetail.setUpdatedAt(product.getUpdatedAt());
        productDetail.setProductType(getProductClass().getSimpleName());
    }

    default ProductSummary mapSummary(P product) {
        if (product == null) {
            return null;
        }

        ProductSummary productSummary = new ProductSummary();
        productSummary.setId(product.getId());
        productSummary.setTitle(product.getTitle());
        productSummary.setOriginalValue(product.getOriginalValue());
        productSummary.setCurrentPrice(product.getCurrentPrice());
        productSummary.setStockQuantity(product.getStockQuantity());
        productSummary.setProductType(getProductClass().getSimpleName());
        productSummary.setCreators(mapCreators(product));
        productSummary.setStatus(product.getStatus().name());
        productSummary.setImageURL(product.getImageURL());

        return productSummary;
    }
}
