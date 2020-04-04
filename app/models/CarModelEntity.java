package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModelEntity {

    private Long id;
    private String name;
    private LocalDateTime startYear;
    private LocalDateTime endYear;

}
