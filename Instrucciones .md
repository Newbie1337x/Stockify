# Instrucciones TP

Fecha de creación: 10 de mayo de 2025 19:40
Revisado: No
Clase: Programacion III

# 🧾 LISTADO DE COSAS QUE TENÉS QUE HACER - RESTful CRUD

---

### 1️⃣ DTO DE ENTRADA (RequestDTO)

📄 Crear `NombreDeTuClaseRequestDTO.java` en `dto/request/`

🔧 Instrucciones:

- Incluir **solo los campos necesarios para crear o actualizar** (⚠️ **nunca el ID**).
- Agregar validaciones con anotaciones según el tipo de dato:
    - `@NotNull`
    - `@NotBlank`
    - `@Size`
    - `@Email`
    - `@Min`, `@Max`
    - etc.

---

### 2️⃣ DTO DE SALIDA (ResponseDTO)

📄 Crear `NombreDeTuClaseResponseDTO.java` en `dto/response/`

🔧 Instrucciones:

- Incluir **todos los campos que se quieran devolver al frontend** (✅ **incluido el ID**).

---

### 3️⃣ MAPPER (MapStruct)

📄 Crear `NombreDeTuClaseMapper.java` en `mapper/`

🧰 Usar **MapStruct** con la siguiente estructura:

```java
@Mapper(componentModel = "spring")
public interface NombreDeTuClaseMapper {
    NombreDeTuClase toEntity(NombreDeTuClaseRequestDTO dto);
    NombreDeTuClaseResponseDTO toResponseDTO(NombreDeTuClase entity);
    List<NombreDeTuClaseResponseDTO> toResponseDTOList(List<NombreDeTuClase> entities);
}

```

---

### 4️⃣ REPOSITORY

📄 Crear `NombreDeTuClaseRepository.java` en `repository/`

🧰 Debe extender de:

```java
JpaRepository<NombreDeTuClase, Long>
```

---

### 5️⃣ SERVICE

📄 Crear `NombreDeTuClaseService.java` en `service/`

🧠 Métodos obligatorios a implementar:

```java
List<ResponseDTO> findAll();
ResponseDTO findById(Long id);
ResponseDTO save(RequestDTO dto);
ResponseDTO update(Long id, RequestDTO dto);
void delete(Long id);
```

📌 Cada método debe incluir:

- ✅ Validación de existencia
- ❗ Verificación de nulos
- ⚙️ Lógica de negocio si aplica
- 🛑 Manejo de errores con descripciones claras y códigos HTTP correctos

⚠️ **Nunca debe lanzarse un error 500**.

Si algo falla (por ejemplo `findById` con ID inexistente), lanzar una excepción como `ResourceNotFoundException` con:

- Mensaje claro
- Código `404 Not Found`

---

### 6️⃣ CONTROLLER

📄 Crear `NombreDeTuClaseController.java` en `controller/`

🔗 Endpoints a implementar:

```java
@GetMapping              → findAll()
@GetMapping("/{id}")     → findById(Long id)
@PostMapping             → save(@Valid @RequestBody RequestDTO dto)
@PutMapping("/{id}")     → update(@PathVariable Long id, @Valid @RequestBody RequestDTO dto)
@DeleteMapping("/{id}")  → delete(Long id)

```

📌 Consideraciones:

- Usar `@Valid` para validar automáticamente los DTO de entrada
- Usar `ResponseEntity<ResponseDTO>` para tener control del status HTTP en las respuestas

---

### 7️⃣ ENDPOINTS OBLIGATORIOS

| Nº | Método | Ruta | Qué hace |
| --- | --- | --- | --- |
| 1 | GET | `/nombredetuclase` | Trae todos |
| 2 | GET | `/nombredetuclase/{id}` | Trae uno por ID |
| 3 | POST | `/nombredetuclase` | Crea uno nuevo |
| 4 | PUT | `/nombredetuclase/{id}` | Actualiza por ID |
| 5 | DELETE | `/nombredetuclase/{id}` | Elimina por ID |

---