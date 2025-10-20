package org.example.common;

import org.example.categorias.exceptions.CategoriaException;
import org.example.funkos.exceptions.FunkoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la API REST.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FunkoException.NotFoundException.class)
    public Map<String, String> handleFunkoNotFoundException( FunkoException.NotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("status", "404");
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoriaException.NotFoundException.class)
    public Map<String, String> handleCategoriaNotFoundException( CategoriaException.NotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("status", "404");
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CategoriaException.ConflictException.class)
    public Map<String, String> handleConflictException( CategoriaException.ConflictException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("status", "409");
        errors.put("error", ex.getMessage());
        return errors;
    }
}
