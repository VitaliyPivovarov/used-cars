package dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarModelDto {

    private Long id;
    private String name;
    private LocalDateTime startYear;
    private LocalDateTime endYear;

}
