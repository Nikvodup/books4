package org.example.web.dto;


import javax.validation.constraints.Pattern;


    public class AuthorToFind {

        @Pattern(regexp = "^[A-Za-z]+$", message = " Author's name. Letters only!")
        private String author;

        private String line="Author not found";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

        public String getLine() {
            return line;
        }
    }
