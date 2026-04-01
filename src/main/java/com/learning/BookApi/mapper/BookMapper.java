package com.learning.BookApi.mapper;

import com.learning.BookApi.dtos.intigration.ExternalBookDto;
import com.learning.BookApi.dtos.response.BookResponse;
import com.learning.BookApi.dtos.response.ReviewedBookListResponse;
import com.learning.BookApi.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {


    @Mapping(source = "title", target = "title")
    @Mapping(source = "firstPublishYear", target = "firstPublishYear")
    @Mapping(source = "authorName", target = "author") // MapStruct will use the method below
    @Mapping(source = "key", target = "key") // Don't forget the ID!
    BookResponse toSingleInternalDto(ExternalBookDto externalBookDto);

    List<BookResponse> toInternalDtoList(List<ExternalBookDto> externalBookDtos);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "review.stars", target = "stars")
    @Mapping(source = "review.comment", target = "comment")
    @Mapping(source = "author", target = "author")
    ReviewedBookListResponse toSingleResponse(Book book);

    List<ReviewedBookListResponse> toResponse(List<Book> bookList);


    default String authorListToString(List<String> authorNames) {
        if (authorNames == null || authorNames.isEmpty()) {
            return "Unknown Author";
        }
        //Return the first author found in the list
        return authorNames.get(0);
    }
}