package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TrackRequest {
    @NotBlank(message = "Track title is required")
    private String title;
    @NotNull(message = "Track length is required")
    @Positive(message = "Track length must be positive")
    private Integer length;
}
