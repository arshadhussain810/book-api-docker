package com.learning.BookApi.exception;


public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException (String message){
        super(message);
    }
}
