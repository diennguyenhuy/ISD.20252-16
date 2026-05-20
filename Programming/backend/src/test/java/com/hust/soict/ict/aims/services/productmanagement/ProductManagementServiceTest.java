package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.BookDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.product.ProductStatus;
import com.hust.soict.ict.aims.models.entities.product.CoverType;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private BookDetail sampleBookDetail;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleCreateRequest = new CreateProductRequest();
        sampleCreateRequest.setTitle("Clean Code");
        sampleCreateRequest.setBarcode("9780132350884");
        sampleCreateRequest.setCategory("BOOK");
        sampleCreateRequest.setOriginalValue(200000L);
        sampleCreateRequest.setCurrentPrice(220000L);
        sampleCreateRequest.setStockQuantity(5);
        sampleCreateRequest.setDescription("Non-blank description");
        sampleCreateRequest.setHeight(new BigDecimal("24.0"));
        sampleCreateRequest.setWidth(new BigDecimal("18.0"));
        sampleCreateRequest.setLength(new BigDecimal("3.0"));
        sampleCreateRequest.setWeight(new BigDecimal("0.6"));

        sampleCreateRequest.setPublisher("O'Reilly Media");
        sampleCreateRequest.setPublicationDate(java.time.LocalDate.now());
        sampleCreateRequest.setAuthors(List.of("Robert C. Martin"));
        sampleCreateRequest.setCoverType("HARDCOVER");
        sampleCreateRequest.setNumberOfPages(300);
        sampleCreateRequest.setGenre("Technology");

        sampleUpdateRequest = new UpdateProductRequest();
        sampleUpdateRequest.setTitle("Clean Code - Ed.2");
        sampleUpdateRequest.setCurrentPrice(210000L);
        sampleUpdateRequest.setStockQuantity(12);
        sampleUpdateRequest.setDescription("Updated description");

        mockProduct = mock(Book.class);
        lenient().when(mockProduct.getId()).thenReturn(sampleId);
        lenient().when(mockProduct.getTitle()).thenReturn("Clean Code");
        lenient().when(mockProduct.getBarcode()).thenReturn("9780132350884");
        lenient().when(mockProduct.getCategory()).thenReturn("BOOK");
        lenient().when(mockProduct.getDescription()).thenReturn("Non-blank description");
        lenient().when(mockProduct.getOriginalValue()).thenReturn(200000L);
        lenient().when(mockProduct.getCurrentPrice()).thenReturn(220000L);
        lenient().when(mockProduct.getStockQuantity()).thenReturn(5);
        lenient().when(mockProduct.getStatus()).thenReturn(ProductStatus.ACTIVE);

        lenient().when(mockProduct.getHeight()).thenReturn(new BigDecimal("24.0"));
        lenient().when(mockProduct.getWidth()).thenReturn(new BigDecimal("18.0"));
        lenient().when(mockProduct.getLength()).thenReturn(new BigDecimal("3.0"));
        lenient().when(mockProduct.getWeight()).thenReturn(new BigDecimal("0.6"));

        Book mockBook = (Book) mockProduct;
        lenient().when(mockBook.getPublisher()).thenReturn("O'Reilly Media");
        lenient().when(mockBook.getPublicationDate()).thenReturn(java.time.LocalDate.now());
        lenient().when(mockBook.getLanguage()).thenReturn("English");
        lenient().when(mockBook.getAuthors()).thenReturn(List.of("Robert C. Martin"));
        lenient().when(mockBook.getCoverType()).thenReturn(CoverType.HARDCOVER);
        lenient().when(mockBook.getNumberOfPages()).thenReturn(464);
        lenient().when(mockBook.getGenre()).thenReturn("Technology");

        sampleBookDetail = new BookDetail();
        sampleBookDetail.setId(sampleId.toString());
        sampleBookDetail.setCategory("BOOK");
    }

    // SUITE 1: CREATE PRODUCT
    @Nested
    @DisplayName("Suite: Create Product Use Case")
    class CreateProductTestSuite {

        @Test
        @DisplayName("Create Product - Success Flow")
        void createProduct_Success() {
            when(productRepo.existsByBarcode(sampleCreateRequest.getBarcode())).thenReturn(false);
            when(productRepo.save(any(Product.class))).thenReturn(mockProduct);
            when(productMapper.toProductDetail(any(Product.class))).thenReturn(sampleBookDetail);

            ProductDetail result = productManagementService.createProduct(sampleCreateRequest);

            assertNotNull(result);
            assertEquals(sampleId.toString(), result.getId());
            // Cập nhật Assert để xác thực trường productType mới thay thế cho category cũ
            assertEquals("BOOK", result.getProductType());
            verify(productRepo, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Create Product - Fail Due To Duplicate Barcode")
        void createProduct_ThrowsException_WhenBarcodeExists() {
            when(productRepo.existsByBarcode(sampleCreateRequest.getBarcode())).thenReturn(true);

            ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
                productManagementService.createProduct(sampleCreateRequest);
            });

            assertEquals("barcode", exception.getInvalidFieldName());
            verify(productRepo, never()).save(any(Product.class));
        }
    }

    // SUITE 2: UPDATE PRODUCT
    @Nested
    @DisplayName("Suite: Update Product Use Case")
    class UpdateProductTestSuite {

        @Test
        @DisplayName("Update Product - Success Flow")
        void updateProduct_Success() {
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
            when(productRepo.save(any(Product.class))).thenReturn(mockProduct);
            when(productMapper.toProductDetail(any(Product.class))).thenReturn(sampleBookDetail);

            ProductDetail result = productManagementService.updateProduct(sampleId, sampleUpdateRequest);

            assertNotNull(result);
            assertEquals("BOOK", result.getProductType());
            verify(productRepo, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Update Product - Fail When Product Not Found")
        void updateProduct_ThrowsException_WhenProductNotFound() {
            UUID missingId = UUID.randomUUID();
            when(productRepo.findById(missingId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {
                productManagementService.updateProduct(missingId, sampleUpdateRequest);
            });
        }

        @Test
        @DisplayName("Update Product - Boundary Failure When Price Too Low (< 30%)")
        void updateProduct_ThrowsException_WhenPriceBelowThirtyPercent() {
            sampleUpdateRequest.setCurrentPrice(50000L);
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
            assertThrows(ProductConstructionException.class, () -> {
                productManagementService.updateProduct(sampleId, sampleUpdateRequest);
            });
        }

        @Test
        @DisplayName("Update Product - Boundary Failure When Price Too High (> 150%)")
        void updateProduct_ThrowsException_WhenPriceAboveOneHundredFiftyPercent() {
            sampleUpdateRequest.setCurrentPrice(350000L);
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

            assertThrows(ProductConstructionException.class, () -> {
                productManagementService.updateProduct(sampleId, sampleUpdateRequest);
            });
        }
    }

    // SUITE 3: DELETE PRODUCT
    @Nested
    @DisplayName("Suite: Delete Product Use Case")
    class DeleteProductTestSuite {

        @Test
        @DisplayName("Delete Product - Fail When Batch Size Exceeds 10")
        void deleteProducts_ThrowsException_WhenBatchSizeIsEleven() {
            List<UUID> invalidSizeList = new ArrayList<>(Collections.nCopies(11, UUID.randomUUID()));

            assertThrows(ProductValidationException.class, () -> {
                productManagementService.deleteProducts(invalidSizeList);
            });
        }

        @Test
        @DisplayName("Delete Product - Success Via Soft Delete To DELETED (Stock = 0)")
        void deleteProducts_SoftDeleteToDeleted_WhenStockIsZero() {
            when(mockProduct.getStockQuantity()).thenReturn(0);
            when(productRepo.countByStatusIn(anyCollection())).thenReturn(0L);
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
            when(mockProduct.getStatus()).thenReturn(ProductStatus.DELETED);

            productManagementService.deleteProducts(List.of(sampleId));

            verify(productRepo, never()).deleteById(any(UUID.class));
            verify(productRepo, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Delete Product - Success Via Soft Delete To DEACTIVATED (Stock > 0)")
        void deleteProducts_SoftDeleteToDeactivated_WhenStockStillExists() {
            when(mockProduct.getStockQuantity()).thenReturn(5);
            when(productRepo.countByStatusIn(anyCollection())).thenReturn(0L);
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
            when(mockProduct.getStatus()).thenReturn(ProductStatus.DEACTIVATED);

            productManagementService.deleteProducts(List.of(sampleId));

            verify(productRepo, never()).deleteById(any(UUID.class));
            verify(productRepo, times(1)).save(any(Product.class));
        }
    }
}