package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderResponseDTO {

    private Long id;
    private String razonSocial;
    private String cuit;
    private String direccionFiscal;
    private String telefono;
    private String mail;
    private String name;

}
