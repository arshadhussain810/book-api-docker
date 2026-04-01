package com.learning.BookApi.service;

import com.learning.BookApi.client.BookClient;
import com.learning.BookApi.dtos.intigration.ExternalBookDto;
import com.learning.BookApi.dtos.response.BookResponse;
import com.learning.BookApi.dtos.response.ReviewCreateRequest;
import com.learning.BookApi.dtos.response.ReviewUpdateRequest;
import com.learning.BookApi.dtos.response.ReviewedBookListResponse;
import com.learning.BookApi.entity.Review;
import com.learning.BookApi.exception.*;
import com.learning.BookApi.mapper.BookMapper;
import com.learning.BookApi.repository.BookRepository;
import com.learning.BookApi.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.learning.BookApi.entity.Book;
import java.util.List;

@Service
public class BookService {

    private BookClient bookClient;
    private BookMapper bookMapper;
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CacheManager cacheManager;

    @Autowired
    public BookService(BookClient bookClient, BookMapper bookMapper,
                       BookRepository bookRepository, ReviewRepository reviewRepository, CacheManager cacheManager) {
        this.bookClient = bookClient;
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.reviewRepository = reviewRepository;
        this.cacheManager = cacheManager;
    }


    public List<BookResponse> getBookFromClient(String query){
        List<ExternalBookDto> externalBookDto =  bookClient.fetchBook(query);

        //Cache bucket for staging(get if exists/create if it does not)
        Cache stagingCache = cacheManager.getCache("externalBookStaging");

        if(externalBookDto.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        if(stagingCache!= null){
            for (ExternalBookDto book: externalBookDto){
                stagingCache.put(book.getKey(), book);
            }
        }
        return bookMapper.toInternalDtoList(externalBookDto);
    }

    //Helper method for add book and add review, tries cache first else hit external api
    public ExternalBookDto getStageOrExternalBook(String bookId){

        //checking redis Cache
        Cache stagingCache = cacheManager.getCache("externalBookStaging");

        ExternalBookDto externalBookDto = (stagingCache != null)?
                stagingCache.get(bookId, ExternalBookDto.class) : null;


        if(externalBookDto == null){
            System.out.println("--CACHE MISSED: HITTING EXTERNAL BOOK LIBRARY--");
            externalBookDto = bookClient.fetchBookById(bookId);
            if (externalBookDto == null){
                throw new IncorrectIdException("Incorrect Book id: " + bookId);
            }
        }



        return externalBookDto;
    }

    @Transactional
    public void addBookToLibrary(String bookId){

        if(bookRepository.existsById(bookId)){
            throw new DuplicateBookException("Book with id "+ bookId+",already exists");
        }

        //Calling the helper method
        ExternalBookDto externalBookDto = getStageOrExternalBook(bookId);

        bookRepository.save(new Book(externalBookDto.getKey(),
                externalBookDto.getTitle(),
                externalBookDto.getAuthorName().get(0),
                externalBookDto.getFirstPublishYear()));
    }

    @Transactional
    public void createBookReview(ReviewCreateRequest reviewCreateRequest){

        //Check if book and review already exist.
        Book book = bookRepository.findById(reviewCreateRequest.getBookId()).orElseGet(() ->
                {
                    ExternalBookDto externalBookDto = getStageOrExternalBook(reviewCreateRequest.getBookId());
                    Book newBook = new Book(externalBookDto.getKey(),
                            externalBookDto.getTitle(),
                            externalBookDto.getAuthorName().get(0),
                            externalBookDto.getFirstPublishYear());
                    return bookRepository.save(newBook);
                });

        //if book is already reviewed
        if (book.getReview() != null){
            throw new DuplicateReviewException("A review already exists for this book");
        }

        Review review = new Review(reviewCreateRequest.getStars(), reviewCreateRequest.getComment());
        book.addReview(review);

        bookRepository.save(book);
    }



    @Cacheable(value = "reviewedBooks", key = "'all_reviews'")
    public List<ReviewedBookListResponse> getReviewBookList(){

        List<Book> bookList = bookRepository.findAll();
        return bookMapper.toResponse(bookList);
    }

    @Transactional
    public void updateBookReview(ReviewUpdateRequest updateRequest){
        Book book = bookRepository.findById(updateRequest.getBookId()).orElseThrow(
                ()-> new BookNotFoundException("Cannot update review: book not found"));

        /*System.out.println(book.getReview().getComment());*/

        book.getReview().setStars(updateRequest.getStars());
        book.getReview().setComment(updateRequest.getComment());

    }

    @Transactional
    public void deleteReviewByBookId(String bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->
                new BookNotFoundException("Incorrect Book id"));

        if (book.getReview() == null){
            throw new ReviewNotFoundException("No review found for the book id " + bookId);
        }

        Integer reviewId = book.getReview().getId();
        book.setReview(null);
        reviewRepository.deleteById(reviewId);

    }

    @Transactional
    public void deleteBookById(String bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->
                new BookNotFoundException("Incorrect Book id"));

        bookRepository.deleteById(bookId);
    }
}
