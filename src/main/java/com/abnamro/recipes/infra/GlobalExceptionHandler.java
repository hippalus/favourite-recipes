package com.abnamro.recipes.infra;

import com.abnamro.recipes.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handle(final ResourceNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handle(final ConstraintViolationException exception) {
        log.error("Exception has been occurred", exception);

        final String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(GlobalExceptionHandler::violationMessage)
                .collect(Collectors.joining(" && "));

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
    }


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull final MethodArgumentNotValidException ex,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {

        log.debug("Method argument not valid. Message: $methodArgumentNotValidException.message", ex);

        final ProblemDetail problemDetail = createFieldErrorResponse(ex.getBindingResult());

        return ResponseEntity.status(status).body(problemDetail);
    }

    private static ProblemDetail createFieldErrorResponse(final Errors bindingResult) {
        final String errorMessage = bindingResult
                .getFieldErrors().stream()
                .map(fieldError -> "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(" && "));

        log.debug("Exception occurred while request validation: {}", errorMessage);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Wrong fields : " + errorMessage);
    }

    private static String violationMessage(final ConstraintViolation<?> violation) {
        return violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage();
    }

}
