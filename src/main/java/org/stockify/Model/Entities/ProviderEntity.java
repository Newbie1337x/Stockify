package org.stockify.Model.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name= "proveedores")
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProveedor",nullable = false)
    private Long id;
    @Column(name = "razon_social",nullable = false, length = 254)
    private String razonSocial;
    @Column(nullable = false, length = 11)
    private String CUIT;
    @Column(name = "direccion_fiscal",nullable = false,length = 254)
    private String direccionFiscal;
    @Column
    private String telefono;
    @Column(nullable = false)
    private String mail;
}
