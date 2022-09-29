package edu.vtc.kurs.dto;

import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

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
