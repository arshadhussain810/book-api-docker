package com.learning.BookApi.dtos.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewUpdateRequest {

    @NotBlank(message = "book key is required")
    private String bookId;

    @NotNull
    @Max(value = 10, message = "Rating can not be higher than 10")
    @Min(value = 0, message = "Rating can not be lower than 0")
    private Integer stars;

    private String comment;

    public ReviewUpdateRequest(String bookId, Integer stars, String comment) {
        this.bookId = bookId;
        this.stars = stars;
        this.comment = comment;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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
}
