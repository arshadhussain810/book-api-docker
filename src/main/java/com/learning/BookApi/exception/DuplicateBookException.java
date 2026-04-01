package com.learning.BookApi.exception;

public class DuplicateBookException extends RuntimeException{
    public DuplicateBookException(String message){
        super(message);
    }
}
