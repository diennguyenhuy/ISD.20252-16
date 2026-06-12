package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCDRequest extends UpdateProductRequest {
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
    @NullOrNotEmpty(message = "Artists must not be empty if provided")
    private List<@NotBlank(message = "Each artist must not be blank if provided") String> artists;
    @NullOrNotBlank(message = "Record label must not be blank if provided")
    private String recordLabel;
    @NullOrNotEmpty(message = "Track list must not be empty if provided")
    private List<@Valid TrackRequest> tracks;
}
