package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.DeleteProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.services.productmanagement.ProductManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductManagementController {

    private final ProductManagementService productManagementService;

    /**
     * Endpoint phục vụ Use Case: Create Product
     * Trả về mã trạng thái 201 Created khi thêm mới thành công
     */
    @PostMapping
    public ResponseEntity<ProductDetail> createProduct(@RequestBody @Valid CreateProductRequest request) {
        ProductDetail createdProduct = productManagementService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Endpoint phục vụ Use Case: Update Product
     * Nhận ID sản phẩm qua PathVariable kiểu UUID để khớp với Service
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDetail> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {
        ProductDetail updatedProduct = productManagementService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Endpoint phục vụ Use Case: Delete Product
     * Bóc tách danh sách List<UUID> từ DeleteProductRequest để truyền vào Service
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody @Valid DeleteProductRequest request) {
        productManagementService.deleteProducts(request.getProductIds());
        return ResponseEntity.noContent().build();
    }
}