package org.stockify.model.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.stockify.dto.response.ErrorResponse;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(DuplicatedUniqueConstraintException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedUniqueConstraintException(
            DuplicatedUniqueConstraintException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSort(PropertyReferenceException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                new InvalidSortParameterException("'Sort' value is invalid.  " + ex.getMessage()), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    /**
     * ⚠️ Simplificado para usuario final:
     * Detecta campos únicos duplicados y devuelve un mensaje entendible.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(HttpServletRequest request,
                                                             DataIntegrityViolationException ex) {
        String raw = ex.getMostSpecificCause().getMessage().toLowerCase();
        String field = "campo único";

        if (raw.contains("barcode")) {
            field = "barcode";
        } else if (raw.contains("name")) {
            field = "nombre";
        } else if (raw.contains("email")) {
            field = "email";
        }

        String msg = "Ya existe un registro con el mismo " + field + ".";

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                new DuplicatedUniqueConstraintException(msg),
                request
        );
    }

    @ExceptionHandler(InvalidSessionStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSessionStatus(
            InvalidSessionStatusException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleNotEnough(
            InsufficientStockException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(TypeNotAcceptedException.class)
    public ResponseEntity<ErrorResponse> handleTypeNotAcceptedException(
            TypeNotAcceptedException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                new BadCredentialsException("El nombre de usuario o la contraseña son incorrectos."),
                request);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientAuthenticationException(
            InsufficientAuthenticationException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    /**
     * Método genérico para construir la respuesta de error.
     */
    private <T extends Throwable> ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, T ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getClass().getSimpleName(),
                status.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
