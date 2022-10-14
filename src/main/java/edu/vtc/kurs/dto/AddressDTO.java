package edu.vtc.kurs.dto;

import edu.vtc.kurs.models.Settlement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * The type Address dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Settlement settlement;
    @NotEmpty(message = "should not be empty")
    private String street;
    @Max(value = 10000, message = "should be less than 10000")
    @Min(value = 1, message = "should be more than 0")
    private int houseNumber;
    @Max(value = 10000, message = "should be less than 10000")
    @Min(value = 0, message = "should be more than 0")
    private int flatNumber;
}
