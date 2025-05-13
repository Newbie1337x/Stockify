package org.stockify.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name= "proveedores")
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor",nullable = false)
    private Long id;

    @Column(name = "razon_social",nullable = false)
    private String razonSocial;

    @Column(nullable = false, length = 100)
    private String cuit;

    @Column(name = "direccion_fiscal",nullable = false)
    private String direccionFiscal;

    @Column
    private String telefono;

    @Column(nullable = false)
    private String mail;

    @Column(name = "nombre_contacto",nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean activo = true;
}
