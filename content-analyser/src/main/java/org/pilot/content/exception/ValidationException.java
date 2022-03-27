package org.pilot.content.exception;

import org.pilot.content.errorhandling.ValidationMessage;

import java.util.List;

/**
 * Indicate validation errors which are not handled via bean validation,
 * other custom and complex validation
 * e.g. during file or content parsing
 */
@SuppressWarnings("serial")
public class ValidationException extends BusinessException {

    private List<ValidationMessage> validationMessages;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<ValidationMessage> validationMessages) {
        super(message);
        this.validationMessages = validationMessages;
    }

    public List<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }

}