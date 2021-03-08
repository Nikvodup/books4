package org.example.app.exceptions;

public class NoFileFoundException extends Exception {
    private final String message;
    public NoFileFoundException(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
