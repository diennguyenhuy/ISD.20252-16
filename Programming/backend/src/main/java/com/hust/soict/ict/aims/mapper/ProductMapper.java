package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE // 🔥 FIX
)
public interface ProductMapper {

    // ===== FIX: dùng default method thay vì MapStruct =====
    default ProductDetail toProductDetail(Product product) {
        if (product instanceof Book b) return toBookDetail(b);
        if (product instanceof Newspaper n) return toNewspaperDetail(n);
        if (product instanceof CD cd) return toCDDetail(cd);
        if (product instanceof DVD dvd) return toDVDDetail(dvd);
        throw new RuntimeException("Unknown product type");
    }

    // ===== REMOVE productType mapping =====
    BookDetail toBookDetail(Book book);

    NewspaperDetail toNewspaperDetail(Newspaper newspaper);

    CDDetail toCDDetail(CD cd);

    DVDDetail toDVDDetail(DVD dvd);

    TrackDetail toTrackDetail(Track track);

    // ===== FIX: remove creators + productType nếu không tồn tại =====
    ProductSummary toProductSummary(Product product);

    // ===== OPTIONAL: giữ lại nếu bạn thực sự có field =====
    @Named("mapProductType")
    default String mapProductType(Product product) {
        return switch (product) {
            case Book ignored -> "Book";
            case Newspaper ignored -> "Newspaper";
            case CD ignored -> "CD";
            case DVD ignored -> "DVD";
            default -> "Unknown";
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