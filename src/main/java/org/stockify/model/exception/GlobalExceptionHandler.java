package org.stockify.model.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.stockify.dto.response.ErrorResponse;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(DuplicatedUniqueConstraintException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedUniqueConstraintException(
            DuplicatedUniqueConstraintException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    // Catch bad request on Sorts





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