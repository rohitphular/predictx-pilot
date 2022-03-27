package org.pilot.content.exception;

/**
 * Common superclass for business exception in the application
 */
public class BusinessException extends Exception {

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Exception exception) {
        super(exception);
    }

    public BusinessException(String msg, Exception exception) {
        super(msg, exception);
    }
}