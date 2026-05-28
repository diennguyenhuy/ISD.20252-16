package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateBookRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateBookRequest;
import com.hust.soict.ict.aims.models.dto.response.product.BookDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.entities.product.Book;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.product.Product.Status;
import com.hust.soict.ict.aims.models.entities.product.*;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    // Sử dụng @Spy để Inject logic thật của Factory vào Service
    @Spy
    private ProductFactory productFactory = new ProductFactory();

    @InjectMocks
    private ProductManagementService productManagementService;

    // Sử dụng trực tiếp class con (CreateBookRequest) để test tính đa hình
    private CreateBookRequest sampleCreateRequest;
    private UpdateBookRequest sampleUpdateRequest;
    private UUID sampleId;
    private Product mockProduct;
    private BookDetail sampleBookDetail;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        // 1. Cài đặt DTO Tạo mới (Sử dụng CreateBookRequest)
        sampleCreateRequest = new CreateBookRequest();
        sampleCreateRequest.setProductType("BOOK");
        sampleCreateRequest.setTitle("Clean Architecture");
        sampleCreateRequest.setCategory("Technology");
        sampleCreateRequest.setDescription("Software design book");
        sampleCreateRequest.setBarcode("9780134494166");
        sampleCreateRequest.setOriginalValue(300000L);
        sampleCreateRequest.setCurrentPrice(320000L);
        sampleCreateRequest.setStockQuantity(10);
        sampleCreateRequest.setHeight(new BigDecimal("23.0"));
        sampleCreateRequest.setWidth(new BigDecimal("15.0"));
        sampleCreateRequest.setLength(new BigDecimal("3.5"));
        sampleCreateRequest.setWeight(new BigDecimal("0.8"));

        // Specific info cho Book
        sampleCreateRequest.setPublisher("Prentice Hall");
        sampleCreateRequest.setPublicationDate(LocalDate.of(2017, 9, 10));
        sampleCreateRequest.setLanguage("English");
        sampleCreateRequest.setAuthors(List.of("Robert C. Martin"));
        sampleCreateRequest.setCoverType("PAPERBACK");
        sampleCreateRequest.setNumberOfPages(432);
        sampleCreateRequest.setGenre("Computer Science");

        // 2. Cài đặt DTO Cập nhật (Sử dụng UpdateBookRequest)
        sampleUpdateRequest = new UpdateBookRequest();
        sampleUpdateRequest.setProductType("BOOK");
        sampleUpdateRequest.setTitle("Clean Architecture - Revised Edition");
        sampleUpdateRequest.setCurrentPrice(310000L); // Dùng wrapper Long
        sampleUpdateRequest.setStockQuantity(15);    // Dùng wrapper Integer
        sampleUpdateRequest.setNumberOfPages(450);   // Update specific field

        // 3. Cấu hình Mock Entity
        mockProduct = mock(Book.class);
        lenient().when(mockProduct.getId()).thenReturn(sampleId);
        lenient().when(mockProduct.getTitle()).thenReturn("Clean Architecture");
        lenient().when(mockProduct.getBarcode()).thenReturn("9780134494166");
        lenient().when(mockProduct.getCategory()).thenReturn("Technology");
        lenient().when(mockProduct.getDescription()).thenReturn("Software design book");
        lenient().when(mockProduct.getOriginalValue()).thenReturn(300000L);
        lenient().when(mockProduct.getCurrentPrice()).thenReturn(320000L);
        lenient().when(mockProduct.getStockQuantity()).thenReturn(10);
        lenient().when(mockProduct.getStatus()).thenReturn(Status.ACTIVE);
        lenient().when(mockProduct.getHeight()).thenReturn(new BigDecimal("23.0"));
        lenient().when(mockProduct.getWidth()).thenReturn(new BigDecimal("15.0"));
        lenient().when(mockProduct.getLength()).thenReturn(new BigDecimal("3.5"));
        lenient().when(mockProduct.getWeight()).thenReturn(new BigDecimal("0.8"));

        Book mockBook = (Book) mockProduct;
        lenient().when(mockBook.getPublisher()).thenReturn("Prentice Hall");
        lenient().when(mockBook.getPublicationDate()).thenReturn(LocalDate.of(2017, 9, 10));
        lenient().when(mockBook.getLanguage()).thenReturn("English");
        lenient().when(mockBook.getAuthors()).thenReturn(List.of("Robert C. Martin"));
        lenient().when(mockBook.getCoverType()).thenReturn(Book.CoverType.PAPERBACK);
        lenient().when(mockBook.getNumberOfPages()).thenReturn(432);
        lenient().when(mockBook.getGenre()).thenReturn("Computer Science");

        // 4. Khởi tạo đối tượng Response mock trả về
        sampleBookDetail = new BookDetail();
        sampleBookDetail.setId(sampleId.toString());
        sampleBookDetail.setTitle("Clean Architecture");

        // Cấu hình mock save() trả về chính đối tượng được truyền vào (phục vụ lấy ClassName)
        lenient().when(productRepo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(productMapper.toProductDetail(any(Product.class))).thenReturn(sampleBookDetail);
    }

    // =========================================================================
    // SUITE 1: CREATE PRODUCT USE CASE
    // =========================================================================
    @Nested
    @DisplayName("Suite: Create Product Use Case")
    class CreateProductTestSuite {

        @Test
        @DisplayName("Create Product - Success Flow with Polymorphic DTO")
        void createProduct_Success() {
            when(productRepo.existsByBarcode(sampleCreateRequest.getBarcode())).thenReturn(false);

            ProductDetail result = productManagementService.createProduct(sampleCreateRequest);

            assertNotNull(result);
            assertEquals("Book", result.getProductType());
            verify(productFactory, times(1)).buildNewProduct(sampleCreateRequest);
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

    // =========================================================================
    // SUITE 2: UPDATE PRODUCT USE CASE
    // =========================================================================
    @Nested
    @DisplayName("Suite: Update Product Use Case")
    class UpdateProductTestSuite {

        @Test
        @DisplayName("Update Product - Success Flow with Polymorphic Fallback")
        void updateProduct_Success() {
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

            ProductDetail result = productManagementService.updateProduct(sampleId, sampleUpdateRequest);

            assertNotNull(result);
            assertEquals("Book", result.getProductType());
            verify(productFactory, times(1)).buildUpdatedProduct(mockProduct, sampleUpdateRequest);
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
            sampleUpdateRequest.setCurrentPrice(50000L); // Original is 300,000
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));

            assertThrows(ProductConstructionException.class, () -> {
                productManagementService.updateProduct(sampleId, sampleUpdateRequest);
            });
        }
    }

    // =========================================================================
    // SUITE 3: DELETE PRODUCT (SOFT DELETE) USE CASE
    // =========================================================================
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
            when(mockProduct.getStatus()).thenReturn(Status.DELETED);

            productManagementService.deleteProducts(List.of(sampleId));

            verify(productRepo, never()).deleteById(any(UUID.class));
            verify(productRepo, times(1)).save(any(Product.class));
            verify(productFactory, times(1)).buildDeletedProduct(mockProduct);
        }

        @Test
        @DisplayName("Delete Product - Success Via Soft Delete To DEACTIVATED (Stock > 0)")
        void deleteProducts_SoftDeleteToDeactivated_WhenStockStillExists() {
            when(mockProduct.getStockQuantity()).thenReturn(5);
            when(productRepo.countByStatusIn(anyCollection())).thenReturn(0L);
            when(productRepo.findById(sampleId)).thenReturn(Optional.of(mockProduct));
            when(mockProduct.getStatus()).thenReturn(Status.DEACTIVATED);

            productManagementService.deleteProducts(List.of(sampleId));

            verify(productRepo, never()).deleteById(any(UUID.class));
            verify(productRepo, times(1)).save(any(Product.class));
            verify(productFactory, times(1)).buildDeletedProduct(mockProduct);
        }
    }
}