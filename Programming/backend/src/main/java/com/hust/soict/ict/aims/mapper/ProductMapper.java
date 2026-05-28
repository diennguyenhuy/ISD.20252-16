package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE // Đổi thành IGNORE để tránh lỗi lặt vặt khi DTO thiếu trường
)
public interface ProductMapper {

    // 1. Các hàm khai báo cho MapStruct tự động sinh code
    BookDetail toBookDetail(Book book);

    NewspaperDetail toNewspaperDetail(Newspaper newspaper);

    CDDetail toCDDetail(CD cd);

    DVDDetail toDVDDetail(DVD dvd);

    TrackDetail toTrackDetail(Track track);

    // 2. Hàm điều hướng đa hình (Thay thế cho @SubclassMapping)
    default ProductDetail toProductDetail(Product product) {
        if (product == null) {
            return null;
        }
        return switch (product) {
            case Book b -> toBookDetail(b);
            case CD cd -> toCDDetail(cd);
            case DVD dvd -> toDVDDetail(dvd);
            case Newspaper n -> toNewspaperDetail(n);
            default -> throw new IllegalArgumentException("Unknown product type");
        };
    }

    // 3. Mapping cho ProductSummary
    @Mapping(target = "creators", expression = "java(mapCreators(product))")
    @Mapping(target = "productType", expression = "java(org.hibernate.Hibernate.getClass(product).getSimpleName().toUpperCase())")
    ProductSummary toProductSummary(Product product);

    // 4. Hàm Helper lấy danh sách Creators
    @Named("mapCreators")
    default List<String> mapCreators(Product product) {
        return switch (product) {
            case Book b -> b.getAuthors();
            case CD cd -> cd.getArtists();
            case DVD dvd -> List.of(dvd.getDirector()); // Chuyển String thành List
            case Newspaper n -> List.of(n.getEditorInChief()); // Chuyển String thành List
            default -> Collections.emptyList();
        };
    }
}