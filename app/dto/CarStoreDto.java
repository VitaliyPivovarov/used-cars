package dto;

import lombok.Data;
import models.CarMarkModel;
import models.CarModelEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarStoreDto {

    private Long id;
    private CarMarkModel carMarkModel;
    private CarModelEntity carModelEntity;
    private LocalDateTime yearOfIssue;
    private int mileage;
    private BigDecimal price;

}
