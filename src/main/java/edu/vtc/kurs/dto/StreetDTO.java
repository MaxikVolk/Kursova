package edu.vtc.kurs.dto;

import edu.vtc.kurs.models.Settlement;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class StreetDTO {
    private Settlement settlement;
    @NotEmpty(message = "should not be empty")
    private String name;
}
