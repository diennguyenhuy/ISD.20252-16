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
    @SubclassMapping(source = Book.class, target = BookDetail.class)
    @SubclassMapping(source = Newspaper.class, target = NewspaperDetail.class)
    @SubclassMapping(source = CD.class, target = CDDetail.class)
    @SubclassMapping(source = DVD.class, target = DVD.class)
    ProductDetail toProductDetail(Product product);

    BookDetail toBookDetail(Book book);

    NewspaperDetail toNewspaperDetail(Newspaper newspaper);

    CDDetail toCDDetail(CD cd);

    DVDDetail toDVDDetail(DVD dvd);

    TrackDetail toTrackDetail(Track track);

    @Mapping(target = "creators", expression = "java(mapCreators(product))")
    @Mapping(target = "productType", expression = "java(org.hibernate.Hibernate.getClass(product).getSimpleName())")
    ProductSummary toProductSummary(Product product);

    @Named("mapCreators")
    default List<String> mapCreators(Product product) {
        return switch (product) {
            case Book b -> b.getAuthors();
            case CD cd -> cd.getArtists();
            default -> Collections.emptyList();
        };
    }
}
