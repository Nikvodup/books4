package org.example.web.dto;


import javax.validation.constraints.Min;

public class BookIdToRemove {

     @Min(value = 1)
    private int id;

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}