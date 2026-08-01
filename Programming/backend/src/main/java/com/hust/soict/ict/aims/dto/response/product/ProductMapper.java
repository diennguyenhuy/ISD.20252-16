package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;
import java.util.function.Supplier;

public abstract class ProductMapper<P extends Product, D extends ProductDetail> {
    private final Supplier<D> detailSupplier;
    final Class<P> productClass;
    
    protected ProductMapper(Supplier<D> detailSupplier, Class<P> productClass) {
        this.detailSupplier = detailSupplier;
        this.productClass = productClass;
    }
    
    public final D map(P product) {
        if (product == null) {
            return null;
        }
        
        D productDetail = detailSupplier.get();
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
        productDetail.setProductType(productClass.getSimpleName());
        mapProduct(product, productDetail);

        return productDetail;
    }

    public final ProductSummary mapSummary(P product) {
        if (product == null) {
            return null;
        }

        return new ProductSummary(
                product.getId(),
                product.getTitle(),
                product.getOriginalValue(),
                product.getCurrentPrice(),
                product.getStockQuantity(),
                productClass.getSimpleName(),
                creators(product),
                product.getImageURL(),
                product.getStatus().name()
        );
    }

    protected abstract void mapProduct(P product, D productDetail);
    protected abstract List<String> creators(P product);
}
