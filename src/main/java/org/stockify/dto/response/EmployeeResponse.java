package org.stockify.dto.response;

import lombok.*;
import org.stockify.model.enums.Status;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private Status status;
    private Boolean active;

}
