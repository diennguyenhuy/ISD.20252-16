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
