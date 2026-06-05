package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TrackRequest {
    @NotBlank(message = "Track title cannot be empty")
    private String title;

    @Positive(message = "Track length must be at least 1 second")
    private Integer length;
}
