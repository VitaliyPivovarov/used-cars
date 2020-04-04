package dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarModelCreateUpdateDto {

    private String name;
    private LocalDateTime startYear;
    private LocalDateTime endYear;

}
