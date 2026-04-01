package com.learning.BookApi.exception;

public class IncorrectIdException extends RuntimeException {
    public IncorrectIdException(String message){
        super(message);
    }
}
