package fr.cleanarchitecture.easyteach.core.domain.exceptions;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(String message) {
        super(message);
    }
}
