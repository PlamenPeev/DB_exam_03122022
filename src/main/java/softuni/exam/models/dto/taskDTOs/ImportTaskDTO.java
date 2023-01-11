package softuni.exam.models.dto.taskDTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTaskDTO {

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement
    private String date;

    @XmlElement
    @NotNull
    private PartIdDTO part;

    @XmlElement
    @NotNull
    private MechanicFirstNameDTO mechanic;

    @XmlElement
    @NotNull
    private CarIdDTO car;

}
