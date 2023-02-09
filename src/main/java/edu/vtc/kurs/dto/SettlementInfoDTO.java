package edu.vtc.kurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * The type Settlement info dto.
 */
@Data
@NoArgsConstructor
public class SettlementInfoDTO {
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Region should not be empty")
    private String region;
    private String description;
    private List<String> photos;
}
