package edu.vtc.kurs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class SettlementDTO {
    @NotEmpty(message = "should not be empty")
    private String name;
    @NotEmpty(message = "should not be empty")
    private String region;
    private String description;
}
