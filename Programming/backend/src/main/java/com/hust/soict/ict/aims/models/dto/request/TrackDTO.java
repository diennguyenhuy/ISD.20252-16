package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackDTO {
    @NotBlank(message = "Track title cannot be empty")
    private String title;

    @Min(value = 1, message = "Track length must be at least 1 second")
    private int length;
}