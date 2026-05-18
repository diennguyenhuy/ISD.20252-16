package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.mapstruct.*;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.DeleteProductRequest;
import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductMapper {
    @Mapping(target = "productType", ignore = true)
    @SubclassMapping(target = BookDetail.class, source = Book.class)
    @SubclassMapping(target = NewspaperDetail.class, source = Newspaper.class)
    @SubclassMapping(target = CDDetail.class, source = CD.class)
    @SubclassMapping(target = DVDDetail.class, source = DVD.class)
    ProductDetail toProductDetail(Product product);

    @Mapping(target = "productType", expression = "java(Book.class.getSimpleName())")
    BookDetail toBookDetail(Book book);

    @Mapping(target = "productType", expression = "java(Newspaper.class.getSimpleName())")
    NewspaperDetail toNewspaperDetail(Newspaper newspaper);

    @Mapping(target = "productType", expression = "java(CD.class.getSimpleName())")
    CDDetail toCDDetail(CD cd);

    @Mapping(target = "productType", expression = "java(DVD.class.getSimpleName())")
    DVDDetail toDVDDetail(DVD dvd);

    TrackDetail toTrackDetail(Track track);

    @Mapping(target = "creators", qualifiedByName = "mapCreators")
    @Mapping(target = "productType", qualifiedByName = "mapProductType")
    ProductSummary toProductSummary(Product product);

// =========================================================================
    // 1. LUỒNG TẠO MỚI (Create Product)
    // =========================================================================

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true) // ID tự sinh ở DB hoặc Service
    @Mapping(target = "status", expression = "java(ProductStatus.ACTIVE)")
    Book toBook(CreateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ProductStatus.ACTIVE)")
    CD toCD(CreateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ProductStatus.ACTIVE)")
    DVD toDVD(CreateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ProductStatus.ACTIVE)")
    Newspaper toNewspaper(CreateProductRequest dto);

    // Hàm điều phối đa hình (Factory Method) sử dụng tính năng Java Pattern Matching Switch
    default Product toEntity(CreateProductRequest dto) {
        if (dto == null) return null;
        return switch (dto.getCategory().toUpperCase()) {
            case "BOOK" -> toBook(dto);
            case "CD" -> toCD(dto);
            case "DVD" -> toDVD(dto);
            case "NEWSPAPER" -> toNewspaper(dto);
            default -> throw new IllegalArgumentException("Unknown category: " + dto.getCategory());
        };
    }

    // =========================================================================
    // 2. LUỒNG CẬP NHẬT (Update Product)
    // Do Entity là Immutable, MapStruct hỗ trợ truyền 2 đối tượng nguồn (product cũ + dto mới)
    // để sinh ra một thực thể mới mang ID cũ giúp Hibernate hiểu đây là lệnh UPDATE.
    // =========================================================================

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Book updateBook(Book product, UpdateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CD updateCD(CD product, UpdateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    DVD updateDVD(DVD product, UpdateProductRequest dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Newspaper updateNewspaper(Newspaper product, UpdateProductRequest dto);

    default Product updateEntityFromDto(UpdateProductRequest dto, Product product) {
        if (dto == null || product == null) return product;
        return switch (product) {
            case Book b -> updateBook(b, dto);
            case CD c -> updateCD(c, dto);
            case DVD d -> updateDVD(d, dto);
            case Newspaper n -> updateNewspaper(n, dto);
            default -> throw new IllegalArgumentException("Unknown product instance type");
        };
    }

    // =========================================================================
    // 3. LUỒNG HỦY KÍCH HOẠT (Delete Product - Chuyển trạng thái mềm)
    // =========================================================================

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "status", expression = "java(ProductStatus.DEACTIVATED)")
    Book deactivateBook(Book book);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "status", expression = "java(ProductStatus.DEACTIVATED)")
    CD deactivateCD(CD cd);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "status", expression = "java(ProductStatus.DEACTIVATED)")
    DVD deactivateDVD(DVD dvd);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "status", expression = "java(ProductStatus.DEACTIVATED)")
    Newspaper deactivateNewspaper(Newspaper newspaper);

    default Product deactivateProduct(Product product) {
        if (product == null) return null;
        return switch (product) {
            case Book b -> deactivateBook(b);
            case CD c -> deactivateCD(c);
            case DVD d -> deactivateDVD(d);
            case Newspaper n -> deactivateNewspaper(n);
            default -> throw new IllegalArgumentException("Unknown product instance type");
        };
    }

    @Named("mapProductType")
    default String mapProductType(Product product) {
        return switch (product) {
            case Book ignored -> Book.class.getSimpleName();
            case Newspaper ignored -> Newspaper.class.getSimpleName();
            case CD ignored -> CD.class.getSimpleName();
            case DVD ignored -> DVD.class.getSimpleName();
            default -> "NotYetOrUnsupportedType";
        };
    }

    @Named("mapCreators")
    default List<String> mapCreators(Product product) {
        return switch (product) {
            case Book b -> b.getAuthors();
            case CD cd -> cd.getArtists();
            default -> Collections.emptyList();
        };
    }
}
