package com.learning.BookApi.client;

import com.learning.BookApi.dtos.intigration.ExternalBookDto;
import com.learning.BookApi.dtos.intigration.OpenLibraryWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class BookClient {

    private final RestTemplate restTemplate;
    private String bookSearchApiUrl = "https://openlibrary.org/search.json?";

    public BookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExternalBookDto> fetchBook(String query){
        //we create the url for open library here
        String url = UriComponentsBuilder.fromUriString(bookSearchApiUrl)
                .queryParam("q",query)
                .queryParam("fields", "key", "title","author_name", "first_publish_year")
                .queryParam("limit", 10)
                .build()
                .toString();

        //we get the response form open library here
        OpenLibraryWrapper bookResponse = restTemplate.getForObject(url, OpenLibraryWrapper.class);

        //we return it as the POJO here
        if(bookResponse != null && bookResponse.getDocs() != null){
            return bookResponse.getDocs();
        }

        return null;
    }

    public ExternalBookDto fetchBookById(String key){
        String url = UriComponentsBuilder.fromUriString(bookSearchApiUrl)
                .queryParam("q","key:"+ key)
                .queryParam("fields", "key", "title","author_name", "first_publish_year")
                .build()
                .toString();

       ;

        OpenLibraryWrapper bookWrapper = restTemplate.getForObject(url, OpenLibraryWrapper.class);
        if (bookWrapper.getDocs().isEmpty()){
            return null;
        }

        return bookWrapper.getDocs().get(0);
    }
}
