package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.mapstruct.*;

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