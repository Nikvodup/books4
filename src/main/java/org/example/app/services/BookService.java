package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    //--------------------find books by author start

    public List<Book> findBookList(String author){
        return bookRepo.findItemList(author);
    }


    public boolean findBooksByAuthor(String author) {
        return findBookList(author).isEmpty();
    }

    //----------------find books by author end

    //----------------find title

    public List<Book> findTitleList(String title) {
        return bookRepo.findTitleList(title);
    }

    public boolean findBookByTitle(String title){
        return findTitleList(title).isEmpty();
    }
}