package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductManagementService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    /**
     * UC: Create Product
     */
    @Transactional
    public ProductDetail createProduct(CreateProductRequest dto) {
        if (productRepo.existsByBarcode(dto.getBarcode())) {
            // SỬA: Truyền đủ 2 tham số (message, invalidFieldName) theo đúng constructor của ProductValidationException
            throw new ProductValidationException("Barcode already exists: " + dto.getBarcode(), "barcode");
        }

        // Chuyển đổi DTO sang Entity thông qua tầng Mapper cấu hình sẵn của nhóm
        Product product = productMapper.toEntity(dto);

        Product savedProduct = productRepo.save(product);
        return mapToProductDetail(savedProduct);
    }

    /**
     * UC: Update Product
     */
    @Transactional
    public ProductDetail updateProduct(UUID id, UpdateProductRequest dto) {
        // SỬA: Chỉ truyền duy nhất 1 tham số id kiểu UUID theo đúng khai báo của ProductNotFoundException
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        long minPrice = (long) (product.getOriginalValue() * 0.3);
        long maxPrice = (long) (product.getOriginalValue() * 1.5);
        if (dto.getCurrentPrice() < minPrice || dto.getCurrentPrice() > maxPrice) {
            // SỬA: Truyền đủ 2 tham số (message, invalidFieldName)
            throw new ProductValidationException("Current price must be between 30% and 150% of original value", "currentPrice");
        }

        // Sử dụng Mapper cập nhật thông tin thay thế cho các hàm Setter bị thiếu
        productMapper.updateEntityFromDto(dto, product);

        Product updatedProduct = productRepo.save(product);
        return mapToProductDetail(updatedProduct);
    }

    /**
     * UC: Delete Product
     */
    @Transactional
    public void deleteProducts(List<UUID> productIds) {
        if (productIds == null || productIds.size() > 10) {
            // SỬA: Truyền đủ 2 tham số (message, invalidFieldName)
            throw new ProductValidationException("Cannot delete more than 10 products at a time.", "productIds");
        }

        // SỬA: Thay thế hàm count không tồn tại bằng phương thức findAllByStatus(status) đã được định nghĩa trong ProductRepository của bạn
        long deletedToday = productRepo.findAllByStatus(ProductStatus.DEACTIVATED).size();

        if (deletedToday + productIds.size() > 20) {
            // SỬA: Truyền đủ 2 tham số (message, invalidFieldName)
            throw new ProductValidationException("Daily deletion quota exceeded (Max 20 per day).", "status");
        }

        for (UUID id : productIds) {
            productRepo.findById(id).ifPresent(product -> {
                if (product.getStockQuantity() == 0) {
                    productRepo.deleteById(id);
                } else {
                    // Cập nhật trạng thái thông qua Mapper/Hàm dựng bản sao bảo mật
                    productMapper.deactivateProduct(product);
                    productRepo.save(product);
                }
            });
        }
    }

    /**
     * Ánh xạ đối tượng sang các lớp con cụ thể của ProductDetail (BookDetail, CDDetail,...)
     */
    private ProductDetail mapToProductDetail(Product product) {
        ProductDetail detail = switch (product.getCategory().toUpperCase()) {
            case "BOOK" -> new BookDetail();
            case "CD" -> new CDDetail();
            case "DVD" -> new DVDDetail();
            case "NEWSPAPER" -> new NewspaperDetail();
            default -> throw new IllegalArgumentException("Unknown product type: " + product.getCategory());
        };

        // Ép các kiểu dữ liệu đặc thù sang dạng String tương thích với DTO của nhóm
        detail.setId(product.getId().toString());
        detail.setTitle(product.getTitle());
        detail.setBarcode(product.getBarcode());
        detail.setCategory(product.getCategory());
        detail.setCurrentPrice(product.getCurrentPrice());
        detail.setStockQuantity(product.getStockQuantity());
        detail.setStatus(product.getStatus().name());

        return detail;
    }
}