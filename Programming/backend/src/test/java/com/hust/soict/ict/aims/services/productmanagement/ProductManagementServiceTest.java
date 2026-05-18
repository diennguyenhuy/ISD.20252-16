package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.product.ProductStatus;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductManagementServiceTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductManagementService productManagementService;

    private CreateProductRequest sampleCreateRequest;
    private UpdateProductRequest sampleUpdateRequest;
    private UUID sampleId;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        // 1. Dữ liệu Request mẫu cho Use Case Create
        sampleCreateRequest = new CreateProductRequest();
        sampleCreateRequest.setTitle("Clean Code");
        sampleCreateRequest.setBarcode("9780132350884");
        sampleCreateRequest.setCategory("BOOK");
        sampleCreateRequest.setOriginalValue(200000L);
        sampleCreateRequest.setCurrentPrice(220000L);
        sampleCreateRequest.setStockQuantity(5);

        // 2. Dữ liệu Request mẫu cho Use Case Update
        sampleUpdateRequest = new UpdateProductRequest();
        sampleUpdateRequest.setTitle("Clean Code - Ed.2");
        sampleUpdateRequest.setCurrentPrice(210000L);
        sampleUpdateRequest.setStockQuantity(12);

        // 3. Giả lập Entity đa hình bằng Mockito
        mockProduct = mock(Book.class);

        // SỬA: Thêm lenient() vào trước các hàm khi khai báo Mock dùng chung trong setUp()
        org.mockito.Mockito.lenient().when(mockProduct.getId()).thenReturn(sampleId);
        org.mockito.Mockito.lenient().when(mockProduct.getTitle()).thenReturn("Clean Code");
        org.mockito.Mockito.lenient().when(mockProduct.getBarcode()).thenReturn("9780132350884");
        org.mockito.Mockito.lenient().when(mockProduct.getCategory()).thenReturn("BOOK");
        org.mockito.Mockito.lenient().when(mockProduct.getOriginalValue()).thenReturn(200000L);
        org.mockito.Mockito.lenient().when(mockProduct.getCurrentPrice()).thenReturn(220000L);
        org.mockito.Mockito.lenient().when(mockProduct.getStockQuantity()).thenReturn(5);
        org.mockito.Mockito.lenient().when(mockProduct.getStatus()).thenReturn(ProductStatus.ACTIVE);
    }

    // =========================================================================
    // 1. TEST CASE CHO USE CASE: CREATE PRODUCT
    // =========================================================================

    @Test
    @DisplayName("Create Product - Success Flow")
    void createProduct_Success() {
        // Given
        when(productRepo.existsByBarcode(sampleCreateRequest.getBarcode())).thenReturn(false);
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(mockProduct);
        when(productRepo.save(any(Product.class))).thenReturn(mockProduct);

        // When
        ProductDetail result = productManagementService.createProduct(sampleCreateRequest);

        // Then
        assertNotNull(result);
        assertEquals(sampleId.toString(), result.getId());
        assertEquals("BOOK", result.getCategory());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Create Product - Fail Due To Duplicate Barcode Exception")
    void createProduct_ThrowsException_WhenBarcodeExists() {
        // Given
        when(productRepo.existsByBarcode(sampleCreateRequest.getBarcode())).thenReturn(true);

        // When & Then
        ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
            productManagementService.createProduct(sampleCreateRequest);
        });

        // Kiểm tra xem message và tên trường dữ liệu lỗi đính kèm đúng không
        assertTrue(exception.getMessage().contains("Barcode already exists"));
        verify(productRepo, never()).save(any(Product.class));
    }

    // =========================================================================
    // 2. TEST CASE CHO USE CASE: UPDATE PRODUCT
    // =========================================================================

    @Test
    @DisplayName("Update Product - Success Flow")
    void updateProduct_Success() {
        // Given
        when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
        when(productRepo.save(any(Product.class))).thenReturn(mockProduct);

        // When
        ProductDetail result = productManagementService.updateProduct(sampleId, sampleUpdateRequest);

        // Then
        assertNotNull(result);
        verify(productMapper, times(1)).updateEntityFromDto(eq(sampleUpdateRequest), eq(mockProduct));
        verify(productRepo, times(1)).save(mockProduct);
    }

    @Test
    @DisplayName("Update Product - Fail When Target Product Id Not Found")
    void updateProduct_ThrowsException_WhenProductNotFound() {
        // Given
        UUID missingId = UUID.randomUUID();
        when(productRepo.findById(missingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productManagementService.updateProduct(missingId, sampleUpdateRequest);
        });
        verify(productRepo, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Update Product - Boundary Failure When New Price Too Low (< 30%)")
    void updateProduct_ThrowsException_WhenPriceBelowThirtyPercent() {
        // Given
        sampleUpdateRequest.setCurrentPrice(50000L); // 50k thấp hơn mức sàn 60k (30% của giá gốc 200k)
        when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

        // When & Then
        ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
            productManagementService.updateProduct(sampleId, sampleUpdateRequest);
        });
        assertTrue(exception.getMessage().contains("between 30% and 150%"));
    }

    @Test
    @DisplayName("Update Product - Boundary Failure When New Price Too High (> 150%)")
    void updateProduct_ThrowsException_WhenPriceAboveOneHundredFiftyPercent() {
        // Given
        sampleUpdateRequest.setCurrentPrice(350000L); // 350k vượt trần 300k (150% của giá gốc 200k)
        when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

        // When & Then
        assertThrows(ProductValidationException.class, () -> {
            productManagementService.updateProduct(sampleId, sampleUpdateRequest);
        });
    }

    // =========================================================================
    // 3. TEST CASE CHO USE CASE: DELETE PRODUCT
    // =========================================================================

    @Test
    @DisplayName("Delete Product - Fail When Selecting More Than 10 Products")
    void deleteProducts_ThrowsException_WhenBatchSizeIsEleven() {
        // Given: Tạo một mảng gồm 11 ID ngẫu nhiên để test dải biên tối đa
        List<UUID> invalidSizeList = new ArrayList<>(Collections.nCopies(11, UUID.randomUUID()));

        // When & Then
        assertThrows(ProductValidationException.class, () -> {
            productManagementService.deleteProducts(invalidSizeList);
        });
    }

    @Test
    @DisplayName("Delete Product - Success Via Hard Delete (Stock Equals 0)")
    void deleteProducts_ExecutesHardDelete_WhenStockIsEmpty() {
        // Given
        when(mockProduct.getStockQuantity()).thenReturn(0); // Cấu hình sản phẩm đã hết sạch trong kho
        when(productRepo.findAllByStatus(ProductStatus.DEACTIVATED)).thenReturn(Collections.emptyList());
        when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

        // When
        productManagementService.deleteProducts(List.of(sampleId));

        // Then: Đảm bảo luồng đi đúng vào nhánh gọi hàm xóa cứng
        verify(productRepo, times(1)).deleteById(sampleId);
        verify(productMapper, never()).deactivateProduct(any(Product.class));
    }

    @Test
    @DisplayName("Delete Product - Success Via Soft Delete / Deactivate (Stock Greater Than 0)")
    void deleteProducts_ExecutesSoftDelete_WhenStockStillExists() {
        // Given
        when(mockProduct.getStockQuantity()).thenReturn(8); // Vẫn còn tồn 8 sản phẩm trong kho
        when(productRepo.findAllByStatus(ProductStatus.DEACTIVATED)).thenReturn(Collections.emptyList());
        when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

        // When
        productManagementService.deleteProducts(List.of(sampleId));

        // Then: Đảm bảo luồng đi đúng nhánh hủy kích hoạt và cập nhật thực thể
        verify(productRepo, never()).deleteById(sampleId);
        verify(productMapper, times(1)).deactivateProduct(mockProduct);
        verify(productRepo, times(1)).save(mockProduct);
    }
}