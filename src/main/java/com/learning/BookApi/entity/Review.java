package com.learning.BookApi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "comment", nullable = true)
    private String comment;

    @OneToOne
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

    public Review( Integer stars, String comment) {

        this.stars = stars;
        this.comment = comment;

    }

    public Review() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
