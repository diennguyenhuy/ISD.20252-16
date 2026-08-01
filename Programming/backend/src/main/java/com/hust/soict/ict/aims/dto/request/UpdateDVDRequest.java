package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDVDRequest extends UpdateProductRequest {
    private UpdateFieldRequest<@NullOrNotBlank(message = "Genre must not be blank if provided") String>
            genre = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotBlank(message = "Director must not be blank if provided") String>
            director = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @Positive(message = "Runtime must be positive if provided") Integer>
            runtime = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotBlank(message = "Studio must not be blank if provided") String>
            studio = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotBlank(message = "Language must not be blank if provided") String>
            language = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<
                @NotEmpty(message = "Subtitles must not be empty if provided")
                        List<@NotBlank(message = "Each subtitle must not be blank if provided") String>
                > subtitles = UpdateFieldRequest.undefined();
}
