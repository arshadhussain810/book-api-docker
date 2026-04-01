package com.learning.BookApi.dtos.response;


public class BookResponse {

    private String key;
    private String title;
    private String author;
    private Integer firstPublishYear;

    public BookResponse(String key, String title, String author, Integer firstPublishYear) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.firstPublishYear = firstPublishYear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }
}
