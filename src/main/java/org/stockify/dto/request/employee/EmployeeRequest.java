package org.stockify.dto.request.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.stockify.model.enums.Status;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequest implements Serializable {
    @NotBlank(message = "An employee required a name")
    private String name;
    @NotBlank()
    @Size(min = 7,max = 8)
    @Pattern(regexp = "^\\d{7,8}$", message = "DNI must consist of digits only and have a length of 7 to 8 digits")
    private String dni;
    @NotBlank(message = "An employee required a lastName")
    private String lastName;
    @NotNull(message = "An employee required confirm his status")
    private Status status;
}
