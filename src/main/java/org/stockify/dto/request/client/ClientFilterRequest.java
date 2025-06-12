package org.stockify.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientFilterRequest {
    private String firstName;
    private String lastName;
    private String dni;
    private String phone;
}
