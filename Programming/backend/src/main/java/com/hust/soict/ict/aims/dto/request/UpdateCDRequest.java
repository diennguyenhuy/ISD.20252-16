package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCDRequest extends UpdateProductRequest {
    private UpdateFieldRequest<@NotBlank(message = "Genre must not be blank if provided") String>
            genre = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<
                @NotEmpty(message = "Artists must not be empty if provided")
                        List<@NotBlank(message = "Each artist must not be blank if provided") String>
                > artists = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotBlank(message = "Record label must not be blank if provided") String>
            recordLabel = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<
                @NotEmpty(message = "Track list must not be empty if provided")
                        List<@Valid TrackRequest>
                > tracks = UpdateFieldRequest.undefined();
}
