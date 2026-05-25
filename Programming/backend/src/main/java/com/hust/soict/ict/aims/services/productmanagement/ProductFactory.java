package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cohesion: Functional Cohesion<br>
 * Reason: All methods strictly focus on a single, well-defined task: constructing and rebuilding Product entities and their specific subclasses.<br>
 * Coupling:
 * - Stamp coupling with CreateProductRequest, UpdateProductRequest, and Product subclasses because composite data objects are passed as parameters and only specific fields are extracted to build entities.
 * Design Strength:
 * Eliminates control coupling (switch-cases) from the service layer by centralizing object instantiation. Utilizes Java Pattern Matching to strictly adhere to the Open/Closed Principle.
 */

@Component
public class ProductFactory {

    public Product buildNewProduct(CreateProductRequest dto) {
        String type = dto.getProductType().toUpperCase();

        return switch (type) {
            case "BOOK" -> new Book.Builder()
                    .title(dto.getTitle()).category(dto.getCategory()).description(dto.getDescription())
                    .height(dto.getHeight()).width(dto.getWidth()).length(dto.getLength()).weight(dto.getWeight())
                    .barcode(dto.getBarcode()).originalValue(dto.getOriginalValue()).currentPrice(dto.getCurrentPrice())
                    .stockQuantity(dto.getStockQuantity()).status(ProductStatus.ACTIVE).imageURL(dto.getImageURL())
                    .publisher(dto.getPublisher()).publicationDate(dto.getPublicationDate()).language(dto.getLanguage())
                    .authors(dto.getAuthors()).coverType(CoverType.valueOf(dto.getCoverType().toUpperCase()))
                    .numberOfPages(dto.getNumberOfPages()).genre(dto.getGenre()).build();

            case "CD" -> {
                List<Track> cdTracks = dto.getTracks() == null ? List.of() : dto.getTracks().stream()
                                                                             .map(t -> new Track(t.getTitle(), t.getLength())).collect(Collectors.toList());
                yield new CD.Builder()
                        .title(dto.getTitle()).category(dto.getCategory()).description(dto.getDescription())
                        .height(dto.getHeight()).width(dto.getWidth()).length(dto.getLength()).weight(dto.getWeight())
                        .barcode(dto.getBarcode()).originalValue(dto.getOriginalValue()).currentPrice(dto.getCurrentPrice())
                        .stockQuantity(dto.getStockQuantity()).status(ProductStatus.ACTIVE).imageURL(dto.getImageURL())
                        .releaseDate(dto.getReleaseDate()).genre(dto.getGenre()).artists(dto.getArtists())
                        .recordLabel(dto.getRecordLabel()).tracks(cdTracks).build();
            }

            case "DVD" -> new DVD.Builder()
                    .title(dto.getTitle()).category(dto.getCategory()).description(dto.getDescription())
                    .height(dto.getHeight()).width(dto.getWidth()).length(dto.getLength()).weight(dto.getWeight())
                    .barcode(dto.getBarcode()).originalValue(dto.getOriginalValue()).currentPrice(dto.getCurrentPrice())
                    .stockQuantity(dto.getStockQuantity()).status(ProductStatus.ACTIVE).imageURL(dto.getImageURL())
                    .releaseDate(dto.getReleaseDate()).genre(dto.getGenre()).discType(DiscType.valueOf(dto.getDiscType().toUpperCase()))
                    .director(dto.getDirector()).runtime(dto.getRuntime()).studio(dto.getStudio()).language(dto.getLanguage())
                    .subtitles(dto.getSubtitles()).build();

            case "NEWSPAPER" -> new Newspaper.Builder()
                    .title(dto.getTitle()).category(dto.getCategory()).description(dto.getDescription())
                    .height(dto.getHeight()).width(dto.getWidth()).length(dto.getLength()).weight(dto.getWeight())
                    .barcode(dto.getBarcode()).originalValue(dto.getOriginalValue()).currentPrice(dto.getCurrentPrice())
                    .stockQuantity(dto.getStockQuantity()).status(ProductStatus.ACTIVE).imageURL(dto.getImageURL())
                    .publisher(dto.getPublisher()).publicationDate(dto.getPublicationDate()).language(dto.getLanguage())
                    .editorInChief(dto.getEditorInChief()).issueNumber(dto.getIssueNumber()).publicationFrequency(dto.getPublicationFrequency())
                    .ISSN(dto.getISSN()).sections(dto.getSections()).build();

            default -> throw new ProductValidationException("Unsupported product type: " + type, "productType");
        };
    }

    public Product buildUpdatedProduct(Product existingProduct, UpdateProductRequest dto) {
        return switch (existingProduct) {
            case Book b -> new Book.Builder()
                    .barcode(b.getBarcode()).category(b.getCategory()).originalValue(b.getOriginalValue()).status(b.getStatus()).imageURL(b.getImageURL()).weight(b.getWeight()).height(b.getHeight()).length(b.getLength()).width(b.getWidth())
                    .publisher(b.getPublisher()).publicationDate(b.getPublicationDate()).language(b.getLanguage()).authors(b.getAuthors()).coverType(b.getCoverType()).numberOfPages(b.getNumberOfPages()).genre(b.getGenre())
                    .title(dto.getTitle()).description(dto.getDescription()).currentPrice(dto.getCurrentPrice()).stockQuantity(dto.getStockQuantity()).build();

            case CD cd -> new CD.Builder()
                    .barcode(cd.getBarcode()).category(cd.getCategory()).originalValue(cd.getOriginalValue()).status(cd.getStatus()).imageURL(cd.getImageURL()).weight(cd.getWeight()).height(cd.getHeight()).length(cd.getLength()).width(cd.getWidth())
                    .releaseDate(cd.getReleaseDate()).genre(cd.getGenre()).artists(cd.getArtists()).recordLabel(cd.getRecordLabel()).tracks(cd.getTracks())
                    .title(dto.getTitle()).description(dto.getDescription()).currentPrice(dto.getCurrentPrice()).stockQuantity(dto.getStockQuantity()).build();

            case DVD d -> new DVD.Builder()
                    .barcode(d.getBarcode()).category(d.getCategory()).originalValue(d.getOriginalValue()).status(d.getStatus()).imageURL(d.getImageURL()).weight(d.getWeight()).height(d.getHeight()).length(d.getLength()).width(d.getWidth())
                    .releaseDate(d.getReleaseDate()).genre(d.getGenre()).discType(d.getDiscType()).director(d.getDirector()).runtime(d.getRuntime()).studio(d.getStudio()).language(d.getLanguage()).subtitles(d.getSubtitles())
                    .title(dto.getTitle()).description(dto.getDescription()).currentPrice(dto.getCurrentPrice()).stockQuantity(dto.getStockQuantity()).build();

            case Newspaper n -> new Newspaper.Builder()
                    .barcode(n.getBarcode()).category(n.getCategory()).originalValue(n.getOriginalValue()).status(n.getStatus()).imageURL(n.getImageURL()).weight(n.getWeight()).height(n.getHeight()).length(n.getLength()).width(n.getWidth())
                    .publisher(n.getPublisher()).publicationDate(n.getPublicationDate()).language(n.getLanguage()).editorInChief(n.getEditorInChief()).issueNumber(n.getIssueNumber()).publicationFrequency(n.getPublicationFrequency()).ISSN(n.getISSN()).sections(n.getSections())
                    .title(dto.getTitle()).description(dto.getDescription()).currentPrice(dto.getCurrentPrice()).stockQuantity(dto.getStockQuantity()).build();

            default -> throw new ProductValidationException("Unknown product instance type", "entity");
        };
    }

    public Product buildDeletedProduct(Product existingProduct) {
        return switch (existingProduct) {
            case Book b -> new Book.Builder()
                    .title(b.getTitle()).barcode(b.getBarcode()).category(b.getCategory()).originalValue(b.getOriginalValue()).currentPrice(b.getCurrentPrice()).stockQuantity(b.getStockQuantity()).description(b.getDescription()).weight(b.getWeight()).height(b.getHeight()).width(b.getWidth()).length(b.getLength()).imageURL(b.getImageURL())
                    .publisher(b.getPublisher()).publicationDate(b.getPublicationDate()).language(b.getLanguage()).authors(b.getAuthors()).coverType(b.getCoverType()).numberOfPages(b.getNumberOfPages()).genre(b.getGenre())
                    .status(existingProduct.getStatus()).build(); // Lưu lại status đã được downgrade (DELETED/DEACTIVATED)

            case CD cd -> new CD.Builder()
                    .title(cd.getTitle()).barcode(cd.getBarcode()).category(cd.getCategory()).originalValue(cd.getOriginalValue()).currentPrice(cd.getCurrentPrice()).stockQuantity(cd.getStockQuantity()).description(cd.getDescription()).weight(cd.getWeight()).height(cd.getHeight()).width(cd.getWidth()).length(cd.getLength()).imageURL(cd.getImageURL())
                    .releaseDate(cd.getReleaseDate()).genre(cd.getGenre()).artists(cd.getArtists()).recordLabel(cd.getRecordLabel()).tracks(cd.getTracks())
                    .status(existingProduct.getStatus()).build();

            case DVD d -> new DVD.Builder()
                    .title(d.getTitle()).barcode(d.getBarcode()).category(d.getCategory()).originalValue(d.getOriginalValue()).currentPrice(d.getCurrentPrice()).stockQuantity(d.getStockQuantity()).description(d.getDescription()).weight(d.getWeight()).height(d.getHeight()).width(d.getWidth()).length(d.getLength()).imageURL(d.getImageURL())
                    .releaseDate(d.getReleaseDate()).genre(d.getGenre()).discType(d.getDiscType()).director(d.getDirector()).runtime(d.getRuntime()).studio(d.getStudio()).language(d.getLanguage()).subtitles(d.getSubtitles())
                    .status(existingProduct.getStatus()).build();

            case Newspaper n -> new Newspaper.Builder()
                    .title(n.getTitle()).barcode(n.getBarcode()).category(n.getCategory()).originalValue(n.getOriginalValue()).currentPrice(n.getCurrentPrice()).stockQuantity(n.getStockQuantity()).description(n.getDescription()).weight(n.getWeight()).height(n.getHeight()).width(n.getWidth()).length(n.getLength()).imageURL(n.getImageURL())
                    .publisher(n.getPublisher()).publicationDate(n.getPublicationDate()).language(n.getLanguage()).editorInChief(n.getEditorInChief()).issueNumber(n.getIssueNumber()).publicationFrequency(n.getPublicationFrequency()).ISSN(n.getISSN()).sections(n.getSections())
                    .status(existingProduct.getStatus()).build();

            default -> existingProduct;
        };
    }
}