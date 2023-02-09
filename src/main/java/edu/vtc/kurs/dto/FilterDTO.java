package edu.vtc.kurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;

/**
 * The type Filter dto.
 */
@Data
@NoArgsConstructor
public class FilterDTO {
    @Nullable
    private String sort;
    @Nullable
    private String region;
    @Nullable
    private String settlement;
    @Nullable
    private String street;
    @Min(value = 0)
    private int houseNumberMore;
    @Min(value = 1)
    private int houseNumberLess;
}
