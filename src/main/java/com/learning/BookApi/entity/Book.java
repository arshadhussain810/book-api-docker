package com.learning.BookApi.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;

@Entity
@Table(name = "book_data")
public class Book {

    @Id
    private String id;

    @Column(name = "book_title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publish_year")
    private Integer publishYear;

    @OneToOne(mappedBy = "book",cascade = CascadeType.ALL)
    private Review review;

    public Book(String id, String title, String author, Integer publishYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
    }

    public Book(){}

    public void addReview(Review review){
        this.review = review;
        review.setBook(this);
    }


    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }
}




