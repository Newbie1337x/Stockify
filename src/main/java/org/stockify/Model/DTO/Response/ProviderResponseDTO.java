package org.stockify.Model.DTO.Response;

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
    private boolean activo;

}
