# Stockify 📌 — *Sistema integral de gestión de inventario y punto de venta.*

**Stockify** es una aplicación construida con **Spring Boot** que permite gestionar inventario, ventas, compras y personal en tiendas o negocios comerciales.

## ✨ Características principales

- 🔹 Gestión completa de productos, categorías y proveedores.
- 🔹 Sistema de punto de venta (POS) con manejo de sesiones y turnos.
- 🔹 Control de inventario y stock en múltiples tiendas.
- 🔹 Gestión de compras, ventas y transacciones.
- 🔹 Administración de empleados y clientes.
- 🔹 Seguridad y autenticación basada en JWT.
- 🔹 Generación de reportes en PDF.

## 🔧 Diseño y patrones aplicados

Este proyecto tiene un enfoque profesional con el objetivo de aplicar distintos conceptos del ecosistema Spring Boot, como:

- Arquitectura por capas (Controladores, Servicios, Repositorios).
- Patrón DTO (Data Transfer Object) para la transferencia de datos entre capas.
- Mapeo de objetos con MapStruct.
- Validaciones con Hibernate Validator.
- Manejo centralizado de excepciones.
- Autenticación y autorización con Spring Security y JWT.
- API RESTful con HATEOAS para mejorar la navegabilidad.
- Documentación de API con OpenAPI/Swagger.

## 🛠️ Tecnologías utilizadas

| Tecnología              | Descripción                                     |
|-------------------------|-------------------------------------------------|
| Spring Boot 3.2.5       | Framework principal                             |
| Java 21                 | Versión del lenguaje                            |
| Spring Data JPA         | Persistencia de datos                           |
| PostgreSQL              | Base de datos                                   |
| Spring Security         | Autenticación y autorización                    |
| JWT                     | Tokens para autenticación                       |
| Lombok                  | Reducción de código boilerplate                 |
| MapStruct               | Mapeo automático entre objetos                  |
| Hibernate Validator     | Validación de datos                             |
| Hibernate Envers        | Auditoría de entidades                          |
| Thymeleaf               | Motor de plantillas                             |
| OpenAPI/Swagger         | Documentación de API                            |
| Spring HATEOAS          | Enlaces hipermedia para API REST                |

## 📁 Estructura del proyecto
```plaintext
org.stockify
│
├── config
│   └── Configuraciones de la aplicación
│
├── controller
│   ├── product
│   │   ├── ProductController
│   │   ├── ProductCategoryController
│   │   ├── ProductProviderController
│   ├── provider
│   │   ├── ProviderController
│   │   ├── ProviderProductController
│   ├── store
│   │   ├── StoreController
│   │   ├── StoreProductController
│   │   ├── StoreStockController
│   ├── transaction
│   │   ├── TransactionController
│   │   ├── TransactionSaleController
│   │   ├── TransactionPurchaseController
│   ├── shift
│   │   ├── ShiftController
│   ├── AuditController
│   ├── CategoryController
│   ├── ClientController
│   ├── EmployeeController
│   ├── PdfGeneratorController
│   ├── PosController
│   ├── PurchaseController
│   ├── SaleController
│   ├── SessionPosController
│   ├── TimeLogController
│   ├── UserManagementController
│
├── dto
│   ├── request
│   ├── response
│
├── model
│   ├── assembler
│   ├── entity
│   ├── exception
│
├── security
│   ├── controller
│   ├── model
│   ├── service
│
├── service
│   ├── Implementación de la lógica de negocio
│
├── util
│   ├── Clases de utilidad
```

## Authors

- [@Newbie1337x](https://github.com/Newbie1337x)
- [@joacoloool](https://github.com/joacoloool)
- [@IgnacioDente](https://github.com/ignaciodente)
- [@Lucasmdv](https://github.com/Lucasmdv)
- [@matias0621](https://github.com/matias0621)
