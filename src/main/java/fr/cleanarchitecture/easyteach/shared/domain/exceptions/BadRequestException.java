package fr.cleanarchitecture.easyteach.shared.domain.exceptions;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(String message) {
        super(message);
    }
}
