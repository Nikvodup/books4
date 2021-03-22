package org.example.web.dto;

import javax.validation.constraints.NotBlank;


public class TitleToFind {

    private  String news="Title not found";

    public String getNews() {
        return news;
    }

    @NotBlank(message="Title is required")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}




