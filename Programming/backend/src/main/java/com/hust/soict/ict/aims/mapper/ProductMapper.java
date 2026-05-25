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
    default ProductDetail toProductDetail(Product product) {
        return switch (product) {
            case Book b -> toBookDetail(b);
            case Newspaper n -> toNewspaperDetail(n);
            case CD cd -> toCDDetail(cd);
            case DVD dvd -> toDVDDetail(dvd);
            default -> throw new IllegalArgumentException("Unsupported product type: " + product.getClass());
        };
    }

    @Mapping(target = "productType", expression = "java(Book.class.getSimpleName())")
    BookDetail toBookDetail(Book book);

    @Mapping(target = "productType", expression = "java(Newspaper.class.getSimpleName())")
    NewspaperDetail toNewspaperDetail(Newspaper newspaper);

    @Mapping(target = "productType", expression = "java(CD.class.getSimpleName())")
    CDDetail toCDDetail(CD cd);

    @Mapping(target = "productType", expression = "java(DVD.class.getSimpleName())")
    DVDDetail toDVDDetail(DVD dvd);

    TrackDetail toTrackDetail(Track track);

    @Mapping(target = "creators", expression = "java(mapCreators(product))")
    @Mapping(target = "productType", expression = "java(mapProductType(product))")
    ProductSummary toProductSummary(Product product);

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
