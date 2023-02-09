package edu.vtc.kurs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * The type Settlement dto.
 */
@Data
@NoArgsConstructor
public class SettlementDTO {
    @NotEmpty(message = "name should not be empty")
    private String name;
    @NotEmpty(message = "region should not be empty")
    private String region;
    private String description;
}
