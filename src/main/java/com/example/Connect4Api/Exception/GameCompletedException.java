package com.example.Connect4Api.Exception;

public class GameCompletedException extends Throwable {

    private String message;

    public GameCompletedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
