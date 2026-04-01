package com.learning.BookApi.dtos.intigration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//This class is used to take the response from open library api
//we need just the doc array which contains the field that we need
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryWrapper {

    //we save it in the ExternalBookDto which is a POJO
    private List<ExternalBookDto> docs;

    public List<ExternalBookDto> getDocs() {
        return docs;
    }

    public void setDocs(List<ExternalBookDto> docs) {
        this.docs = docs;
    }
}

