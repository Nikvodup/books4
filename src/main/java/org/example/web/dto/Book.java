package org.example.web.dto;

import javax.validation.constraints.*;
import javax.validation.constraints.Pattern;

public class Book {
    private Integer id;


    @Pattern(regexp = "^[A-Za-z]+$", message = "Author's name. Letters only!")
    private String author;

    @NotBlank(message = "Title is required")
    private String title;

      @Digits(integer = 6,fraction = 0, message = "Digits only!")
    private Integer size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
