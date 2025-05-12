package org.stockify.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de respuesta utilizado para representar detalles de un error ocurrido durante el procesamiento de una solicitud.
 * Incluye información relevante como el mensaje de error, tipo de error, código de estado HTTP,
 * ruta de la solicitud y marca de tiempo del momento en que ocurrió el error.
 */
public record ErrorResponse(
        String message,
        String error,
        int status,
        String path,
        LocalDateTime timestamp
)
{}

