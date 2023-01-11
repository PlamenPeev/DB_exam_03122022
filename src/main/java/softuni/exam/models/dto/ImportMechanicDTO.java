package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ImportMechanicDTO {

    @Expose
    @Size(min = 2)
    private String firstName;

    @Expose
    @Size(min = 2)
    private String lastName;

    @Expose
    @Email
    private String email;

    @Expose
    @Size(min = 2)
    private String phone;
}
