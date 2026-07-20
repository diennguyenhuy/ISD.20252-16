package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateNewspaperRequest extends UpdatePrintableProductRequest {
    private UpdateFieldRequest<@NotBlank(message = "Editor-in-chief must not be blank if provided") String>
            editorInChief = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NullOrNotBlank(message = "Publication frequency must not be blank if provided") String>
            publicationFrequency = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<
                @NullOrNotEmpty(message = "Sections must not be empty if provided")
                        List<@NotBlank(message = "Each section must not be blank if provided") String>
                > sections = UpdateFieldRequest.undefined();
}
