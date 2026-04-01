package com.learning.BookApi.controller;

import com.learning.BookApi.dtos.response.BookResponse;
import com.learning.BookApi.dtos.response.ReviewCreateRequest;
import com.learning.BookApi.dtos.response.ReviewUpdateRequest;
import com.learning.BookApi.dtos.response.ReviewedBookListResponse;
import com.learning.BookApi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBook(@RequestParam String title){

        List<BookResponse> bookResponse = bookService.getBookFromClient(title);
        System.out.println(bookResponse.isEmpty());
        if (bookResponse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookResponse);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestParam(required = true) String bookId){
        bookService.addBookToLibrary(bookId);
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@Valid @RequestBody(required = true)ReviewCreateRequest createRequest){

        bookService.createBookReview(createRequest);
    }

    @GetMapping("/reviews")
    public List<ReviewedBookListResponse> readBookList(){

        return bookService.getReviewBookList();
    }

    @PatchMapping("/reviews")

    public void updateBookReview(@Valid @RequestBody(required = true) ReviewUpdateRequest updateRequest){
        bookService.updateBookReview(updateRequest);
    }

    @DeleteMapping("/reviews")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookReview(@RequestParam(required = true) String bookId){
        bookService.deleteReviewByBookId(bookId);
    }

    @DeleteMapping("/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@RequestParam(required = true) String bookId){
        bookService.deleteBookById(bookId);
    }
}
