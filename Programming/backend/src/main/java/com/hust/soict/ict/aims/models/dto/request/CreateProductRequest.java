package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateProductRequest {
    // Shared fields
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Height is required")
    @Positive
    private BigDecimal height;

    @NotNull(message = "Width is required")
    @Positive
    private BigDecimal width;

    @NotNull(message = "Length is required")
    @Positive
    private BigDecimal length;

    @NotNull(message = "Weight is required")
    @Positive
    private BigDecimal weight;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotNull(message = "Original value is required")
    @PositiveOrZero
    private Long originalValue;

    @NotNull(message = "Current price is required")
    @PositiveOrZero
    private Long currentPrice;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero
    private Integer stockQuantity;

    private String imageURL;

    // PRINTABLE PRODUCT (BOOK, NEWSPAPER)
    private String publisher;
    private LocalDate publicationDate;
    private String language;

    // BOOK
    private List<String> authors;
    private String coverType;
    private Integer numberOfPages;
    private String genre;

    // CD
    private LocalDate releaseDate;
    private List<String> artists;
    private String recordLabel;
    private List<TrackDTO> tracks;

    // DVD
    private String discType;
    private String director;
    private Integer runtime;
    private String studio;
    private List<String> subtitles;

    // NEWSPAPER
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections;

    @Data
    public static class TrackDTO {
        private String title;
        private Integer length;
    }
}