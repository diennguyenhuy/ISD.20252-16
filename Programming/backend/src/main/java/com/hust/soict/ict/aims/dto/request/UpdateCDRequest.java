package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCDRequest extends UpdateProductRequest {
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
    @NullOrNotEmpty(message = "Artists must not be empty if provided")
    private List<String> artists;
    @NullOrNotBlank(message = "Record label must not be blank if provided")
    private String recordLabel;
}
