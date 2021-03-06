package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class TitleToFind {

    @NotEmpty
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
