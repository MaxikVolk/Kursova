package edu.vtc.kurs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfoDTO {
    @NotEmpty(message = "should not be empty")
    private String name;
    @NotEmpty(message = "should not be empty")
    private String region;
    private String description;
    private List<String> photos;
}
