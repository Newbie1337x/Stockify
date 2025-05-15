package org.stockify.model.exceptionHandlers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.stockify.dto.response.ErrorResponse;
import org.stockify.model.exceptions.ClientNotFoundException;

@RestControllerAdvice
public class ClientExceptionHandler {

    /*@ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body();
    }
*/
}
