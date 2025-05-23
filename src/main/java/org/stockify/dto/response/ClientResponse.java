package org.stockify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientResponse extends RepresentationModel<ClientResponse> {
    private Long id;
    private String firstName;
    private String lastName;
    private int dni;
}