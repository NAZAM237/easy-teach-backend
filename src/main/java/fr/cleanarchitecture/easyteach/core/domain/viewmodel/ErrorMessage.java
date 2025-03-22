package fr.cleanarchitecture.easyteach.core.domain.viewmodel;

public class ErrorMessage {
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
