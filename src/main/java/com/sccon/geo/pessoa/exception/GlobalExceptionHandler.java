package com.sccon.geo.pessoa.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(PessoaNotFoundException.class)
    public ProblemDetail handleNotFound(
            PessoaNotFoundException ex,
            HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));

        enrich(pd);

        return pd;
    }

    // 409
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        pd.setTitle("Conflict");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));

        enrich(pd);

        return pd;
    }

    // ß422
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomain(
            DomainException ex,
            HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pd.setTitle("Business rule violation");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));

        enrich(pd);

        return pd;
    }

    // 400 - Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pd.setTitle("Validation failed");
        pd.setDetail(message);
        pd.setInstance(URI.create(request.getRequestURI()));

        // structured field errors
        pd.setProperty("errors", ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldError(err.getField(), err.getDefaultMessage()))
                .toList()
        );

        enrich(pd);

        return pd;
    }

    // 400 - Enum / type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pd.setTitle("Invalid parameter");
        pd.setInstance(URI.create(request.getRequestURI()));

        if (ex.getRequiredType() != null &&
                ex.getRequiredType().isEnum()) {

            Object[] allowedValues = ex.getRequiredType().getEnumConstants();

            pd.setDetail(String.format(
                    "Invalid value '%s' for parameter '%s'. Allowed: %s",
                    ex.getValue(),
                    ex.getName(),
                    Arrays.toString(allowedValues)
            ));

            pd.setProperty("parameter", ex.getName());
            pd.setProperty("invalidValue", ex.getValue());
            pd.setProperty("allowedValues", allowedValues);

        } else {
            pd.setDetail(ex.getMessage());
        }

        enrich(pd);

        return pd;
    }

    // 500 fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        pd.setTitle("Internal server error");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));

        enrich(pd);

        return pd;
    }

    // Common enrichment (very useful in real systems)
    private void enrich(ProblemDetail pd) {
        pd.setProperty("timestamp", Instant.now());
    }

    // Helper DTO for validation errors
    public record FieldError(String field, String message) {}
}