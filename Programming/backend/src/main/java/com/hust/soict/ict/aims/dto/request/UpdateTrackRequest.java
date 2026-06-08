package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateTrackRequest {
    @NotNull
    private UUID id;
    @NullOrNotBlank(message = "Track title is required if provided")
    private String title;
    @Positive(message = "Track length must be positive if provided")
    private Integer length;
}
