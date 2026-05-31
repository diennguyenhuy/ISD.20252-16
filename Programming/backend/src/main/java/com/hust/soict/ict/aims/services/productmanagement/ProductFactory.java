package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.models.dto.request.*;
import com.hust.soict.ict.aims.models.entities.product.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        return switch (dto) {
            case CreateBookRequest bReq -> new Book.Builder()
                    .title(bReq.getTitle()).category(bReq.getCategory()).description(bReq.getDescription())
                    .height(bReq.getHeight()).width(bReq.getWidth()).length(bReq.getLength()).weight(bReq.getWeight())
                    .barcode(bReq.getBarcode()).originalValue(bReq.getOriginalValue()).currentPrice(bReq.getCurrentPrice())
                    .stockQuantity(bReq.getStockQuantity())
                    .status(Product.Status.ACTIVE) // Sửa thành Product.Status
                    .imageURL(bReq.getImageURL())
                    .publisher(bReq.getPublisher()).publicationDate(bReq.getPublicationDate()).language(bReq.getLanguage())
                    .authors(bReq.getAuthors())
                    .coverType(Book.CoverType.valueOf(bReq.getCoverType().toUpperCase())) // Sửa thành Book.CoverType
                    .numberOfPages(bReq.getNumberOfPages()).genre(bReq.getGenre()).build();

            case CreateCDRequest cdReq -> {
                List<Track> cdTracks = cdReq.getTracks() == null ? List.of() : cdReq.getTracks().stream()
                        .map(t -> new Track(t.getTitle(), t.getLength())).collect(Collectors.toList());
                CD newCd = new CD.Builder()
                        .title(cdReq.getTitle()).category(cdReq.getCategory()).description(cdReq.getDescription())
                        .height(cdReq.getHeight()).width(cdReq.getWidth()).length(cdReq.getLength()).weight(cdReq.getWeight())
                        .barcode(cdReq.getBarcode()).originalValue(cdReq.getOriginalValue()).currentPrice(cdReq.getCurrentPrice())
                        .stockQuantity(cdReq.getStockQuantity())
                        .status(Product.Status.ACTIVE)
                        .imageURL(cdReq.getImageURL())
                        .releaseDate(cdReq.getReleaseDate()).genre(cdReq.getGenre()).artists(cdReq.getArtists())
                        .recordLabel(cdReq.getRecordLabel()).tracks(cdTracks).build();
                if (newCd.getTracks() != null) {
                    newCd.getTracks().forEach(t -> t.setCd(newCd));
                }

                yield newCd;
            }

            case CreateDVDRequest dvdReq -> new DVD.Builder()
                    .title(dvdReq.getTitle()).category(dvdReq.getCategory()).description(dvdReq.getDescription())
                    .height(dvdReq.getHeight()).width(dvdReq.getWidth()).length(dvdReq.getLength()).weight(dvdReq.getWeight())
                    .barcode(dvdReq.getBarcode()).originalValue(dvdReq.getOriginalValue()).currentPrice(dvdReq.getCurrentPrice())
                    .stockQuantity(dvdReq.getStockQuantity())
                    .status(Product.Status.ACTIVE)
                    .imageURL(dvdReq.getImageURL())
                    .releaseDate(dvdReq.getReleaseDate()).genre(dvdReq.getGenre())
                    .discType(DVD.DiscType.valueOf(dvdReq.getDiscType().toUpperCase())) // Sửa thành DVD.DiscType
                    .director(dvdReq.getDirector()).runtime(dvdReq.getRuntime()).studio(dvdReq.getStudio()).language(dvdReq.getLanguage())
                    .subtitles(dvdReq.getSubtitles()).build();

            case CreateNewspaperRequest nReq -> new Newspaper.Builder()
                    .title(nReq.getTitle()).category(nReq.getCategory()).description(nReq.getDescription())
                    .height(nReq.getHeight()).width(nReq.getWidth()).length(nReq.getLength()).weight(nReq.getWeight())
                    .barcode(nReq.getBarcode()).originalValue(nReq.getOriginalValue()).currentPrice(nReq.getCurrentPrice())
                    .stockQuantity(nReq.getStockQuantity())
                    .status(Product.Status.ACTIVE)
                    .imageURL(nReq.getImageURL())
                    .publisher(nReq.getPublisher()).publicationDate(nReq.getPublicationDate()).language(nReq.getLanguage())
                    .editorInChief(nReq.getEditorInChief()).issueNumber(nReq.getIssueNumber()).publicationFrequency(nReq.getPublicationFrequency())
                    .ISSN(nReq.getISSN()) // Sửa typo ISSN -> issn
                    .sections(nReq.getSections()).build();

            default -> throw new ProductValidationException("Unsupported product request type", "productType");
        };
    }

    public Product buildUpdatedProduct(Product existingProduct, UpdateProductRequest dto) {

        // General Info Fallback
        String title = dto.getTitle() != null ? dto.getTitle() : existingProduct.getTitle();
        String category = dto.getCategory() != null ? dto.getCategory() : existingProduct.getCategory();
        String desc = dto.getDescription() != null ? dto.getDescription() : existingProduct.getDescription();
        String img = dto.getImageURL() != null ? dto.getImageURL() : existingProduct.getImageURL();
        long price = dto.getCurrentPrice() != null ? dto.getCurrentPrice() : existingProduct.getCurrentPrice();
        int stock = dto.getStockQuantity() != null ? dto.getStockQuantity() : existingProduct.getStockQuantity();
        BigDecimal h = dto.getHeight() != null ? dto.getHeight() : existingProduct.getHeight();
        BigDecimal w = dto.getWidth() != null ? dto.getWidth() : existingProduct.getWidth();
        BigDecimal l = dto.getLength() != null ? dto.getLength() : existingProduct.getLength();
        BigDecimal weight = dto.getWeight() != null ? dto.getWeight() : existingProduct.getWeight();

        return switch (existingProduct) {
            case Book b -> {
                UpdateBookRequest bDto = (UpdateBookRequest) dto;
                String pub = bDto.getPublisher() != null ? bDto.getPublisher() : b.getPublisher();
                LocalDate pubDate = bDto.getPublicationDate() != null ? bDto.getPublicationDate() : b.getPublicationDate();
                String lang = bDto.getLanguage() != null ? bDto.getLanguage() : b.getLanguage();
                List<String> auths = bDto.getAuthors() != null ? bDto.getAuthors() : b.getAuthors();

                // Gọi qua lớp Book
                Book.CoverType cov = bDto.getCoverType() != null ? Book.CoverType.valueOf(bDto.getCoverType().toUpperCase()) : b.getCoverType();

                int pages = bDto.getNumberOfPages() != null ? bDto.getNumberOfPages() : b.getNumberOfPages();
                String genre = bDto.getGenre() != null ? bDto.getGenre() : b.getGenre();

                yield new Book.Builder()
                        .barcode(b.getBarcode()).originalValue(b.getOriginalValue()).status(b.getStatus())
                        .title(title).category(category).description(desc).imageURL(img).currentPrice(price).stockQuantity(stock)
                        .height(h).width(w).length(l).weight(weight)
                        .publisher(pub).publicationDate(pubDate).language(lang).authors(auths).coverType(cov).numberOfPages(pages).genre(genre)
                        .build();
            }
            case CD cd -> {
                UpdateCDRequest cdDto = (UpdateCDRequest) dto;
                LocalDate relDate = cdDto.getReleaseDate() != null ? cdDto.getReleaseDate() : cd.getReleaseDate();
                String genre = cdDto.getGenre() != null ? cdDto.getGenre() : cd.getGenre();
                List<String> artists = cdDto.getArtists() != null ? cdDto.getArtists() : cd.getArtists();
                String recLabel = cdDto.getRecordLabel() != null ? cdDto.getRecordLabel() : cd.getRecordLabel();

                List<Track> tracks = cd.getTracks();
                if (cdDto.getTracks() != null) {
                    tracks = cdDto.getTracks().stream()
                            .map(t -> new Track(t.getTitle(), t.getLength()))
                            .collect(Collectors.toList());
                }

                CD updatedCd = new CD.Builder()
                        .barcode(cd.getBarcode()).originalValue(cd.getOriginalValue()).status(cd.getStatus())
                        .title(title).category(category).description(desc).imageURL(img).currentPrice(price).stockQuantity(stock)
                        .height(h).width(w).length(l).weight(weight)
                        .releaseDate(relDate).genre(genre).artists(artists).recordLabel(recLabel).tracks(tracks)
                        .build();

                // Link CD with each Track
                if (updatedCd.getTracks() != null) {
                    updatedCd.getTracks().forEach(t -> t.setCd(updatedCd));
                }

                yield updatedCd;
            }
            case DVD d -> {
                UpdateDVDRequest dDto = (UpdateDVDRequest) dto;
                LocalDate relDate = dDto.getReleaseDate() != null ? dDto.getReleaseDate() : d.getReleaseDate();
                String genre = dDto.getGenre() != null ? dDto.getGenre() : d.getGenre();

                // Gọi qua lớp DVD
                DVD.DiscType dt = dDto.getDiscType() != null ? DVD.DiscType.valueOf(dDto.getDiscType().toUpperCase()) : d.getDiscType();

                String dir = dDto.getDirector() != null ? dDto.getDirector() : d.getDirector();
                int run = dDto.getRuntime() != null ? dDto.getRuntime() : d.getRuntime();
                String stu = dDto.getStudio() != null ? dDto.getStudio() : d.getStudio();
                String lang = dDto.getLanguage() != null ? dDto.getLanguage() : d.getLanguage();
                List<String> sub = dDto.getSubtitles() != null ? dDto.getSubtitles() : d.getSubtitles();

                yield new DVD.Builder()
                        .barcode(d.getBarcode()).originalValue(d.getOriginalValue()).status(d.getStatus())
                        .title(title).category(category).description(desc).imageURL(img).currentPrice(price).stockQuantity(stock)
                        .height(h).width(w).length(l).weight(weight)
                        .releaseDate(relDate).genre(genre).discType(dt).director(dir).runtime(run).studio(stu).language(lang).subtitles(sub)
                        .build();
            }
            case Newspaper n -> {
                UpdateNewspaperRequest nDto = (UpdateNewspaperRequest) dto;
                String pub = nDto.getPublisher() != null ? nDto.getPublisher() : n.getPublisher();
                LocalDate pubDate = nDto.getPublicationDate() != null ? nDto.getPublicationDate() : n.getPublicationDate();
                String lang = nDto.getLanguage() != null ? nDto.getLanguage() : n.getLanguage();
                String ed = nDto.getEditorInChief() != null ? nDto.getEditorInChief() : n.getEditorInChief();
                String issue = nDto.getIssueNumber() != null ? nDto.getIssueNumber() : n.getIssueNumber();
                String freq = nDto.getPublicationFrequency() != null ? nDto.getPublicationFrequency() : n.getPublicationFrequency();
                String issnVal = nDto.getISSN() != null ? nDto.getISSN() : n.getISSN(); // Sửa typo ISSN
                List<String> sec = nDto.getSections() != null ? nDto.getSections() : n.getSections();

                yield new Newspaper.Builder()
                        .barcode(n.getBarcode()).originalValue(n.getOriginalValue()).status(n.getStatus())
                        .title(title).category(category).description(desc).imageURL(img).currentPrice(price).stockQuantity(stock)
                        .height(h).width(w).length(l).weight(weight)
                        .publisher(pub).publicationDate(pubDate).language(lang).editorInChief(ed).issueNumber(issue).publicationFrequency(freq).ISSN(issnVal).sections(sec)
                        .build();
            }
            default -> throw new IllegalArgumentException("Unknown product instance type");
        };
    }

    public Product buildDeletedProduct(Product existingProduct) {
        return switch (existingProduct) {
            case Book b -> new Book.Builder()
                    .title(b.getTitle()).barcode(b.getBarcode()).category(b.getCategory()).originalValue(b.getOriginalValue()).currentPrice(b.getCurrentPrice()).stockQuantity(b.getStockQuantity()).description(b.getDescription()).weight(b.getWeight()).height(b.getHeight()).width(b.getWidth()).length(b.getLength()).imageURL(b.getImageURL())
                    .publisher(b.getPublisher()).publicationDate(b.getPublicationDate()).language(b.getLanguage()).authors(b.getAuthors()).coverType(b.getCoverType()).numberOfPages(b.getNumberOfPages()).genre(b.getGenre())
                    .status(existingProduct.getStatus()).build();

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