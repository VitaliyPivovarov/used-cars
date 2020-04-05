package dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarStoreCreateUpdateDto {

    private Long carMarkModelId;
    private Long carModelEntityId;
    private LocalDateTime yearOfIssue;
    private int mileage;
    private BigDecimal price;
}
