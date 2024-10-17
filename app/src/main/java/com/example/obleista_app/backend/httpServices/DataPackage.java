package com.example.obleista_app.backend.httpServices;

public class DataPackage<T> {
    private int status;
    private String message;
    private T data;  // Cambia Object por T

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
