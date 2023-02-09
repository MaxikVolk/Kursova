package edu.vtc.kurs.dto;

import edu.vtc.kurs.models.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * The type Street dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreetDTO {
    private Settlement settlement;
    @NotEmpty(message = "Name should not be empty")
    private String name;
}
