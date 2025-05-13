package org.stockify.Model.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProviderRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    private String name;

    private String telefono;

    @Email(message = "Formato de email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String mail;

    @NotBlank(message = "El CUIT es obligatorio")
    private String cuit;

    private String direccionFiscal;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;
}
