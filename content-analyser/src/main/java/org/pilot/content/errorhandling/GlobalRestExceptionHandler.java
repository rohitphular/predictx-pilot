package org.pilot.content.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.pilot.content.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalRestExceptionHandler {

    private static final String FALLBACK_ERROR_MSG = "Oops! Something went wrong";

    // GENERAL ERROR HANDLING
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleErrors(final Exception ex) {
        log.error("Unknown error received", ex);
        /* Fallback to server error */
        final String errorMessage = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : FALLBACK_ERROR_MSG;
        return new ResponseEntity<>(new WrapperErrorMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // VALIDATION ERROR HANDLING
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(final MethodArgumentNotValidException ex) {
        log.error("Validation errors received", ex);
        final List<ValidationMessage> validationErrors = new ArrayList<>();
        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            validationErrors.add(ValidationMessage.builder()
                    .entity(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build());
        }

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleBadRequestException(final ValidationException ex) {
        log.error("Custom validation errors received", ex);
        if (ex.getValidationMessages() == null || ex.getValidationMessages().isEmpty()) {
            return new ResponseEntity<>(new WrapperErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ex.getValidationMessages(), HttpStatus.BAD_REQUEST);
    }

}