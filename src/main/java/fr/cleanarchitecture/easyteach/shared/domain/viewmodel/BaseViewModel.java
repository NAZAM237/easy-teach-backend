package fr.cleanarchitecture.easyteach.shared.domain.viewmodel;

import java.io.Serializable;

public class BaseViewModel<T> implements Serializable {
    private String message;
    private T data;

    public BaseViewModel() {}

    public BaseViewModel(T data) {
        this.message = "success";
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
