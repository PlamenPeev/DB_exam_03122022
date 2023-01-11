package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter
@Setter
public class ImportPartDTO {

    @Expose
    @Size(min = 2, max = 19)
    private String partName;

    @Expose
    @DecimalMin("10.00")
    @DecimalMax("2000.00")
    private Double price;

    @Expose
    @Positive
    private Integer quantity;
}
