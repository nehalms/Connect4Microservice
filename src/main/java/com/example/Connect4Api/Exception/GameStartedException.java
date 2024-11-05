package com.example.Connect4Api.Exception;

public class GameStartedException extends Throwable {

    private String message;

    public GameStartedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
