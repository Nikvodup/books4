package org.example.app.services;

import org.example.web.dto.Book;

import java.util.List;
import java.util.Map;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    List<Book> findItemList(String author);

    List<Book> findTitleList(String title);


    Map<String, String> findTitleAuthor();
}
