package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarStoreModel {

    private Long id;
    private CarMarkModel carMarkModel;
    private CarModelEntity carModelEntity;
    private LocalDateTime yearOfIssue;
    private int mileage;
    private BigDecimal price;

}
