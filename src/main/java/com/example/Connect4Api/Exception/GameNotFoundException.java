package com.example.Connect4Api.Exception;

public class GameNotFoundException extends Throwable {

    private String message;

    public GameNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
