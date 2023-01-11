package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Column(name = "car_make", nullable = false)
    @Size(min = 2, max = 30)
    private String carMake;

    @Column(name = "car_model", nullable = false)
    @Size(min = 2, max = 30)
    private String carModel;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "plate_number", unique = true)
    @Size(min = 2, max = 30)
    private String plateNumber;

    @Column(nullable = false)
    private Integer kilometers;

    @Column(nullable = false)
    @Min(1)
    private Double engine;
}
