package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.AuthorToFind;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if(book.getAuthor()==null | book.getTitle()==null || book.getSize()==null) {
            book.setId(null);}
        else {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retrieveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return true;
    }

    //-----------------------------------find by author start
    @Override
    public List<Book> findItemList(String author){
        List<Book> matches = new ArrayList<>();
        for (Book book : retrieveAll()) {
            if(book.getAuthor().equalsIgnoreCase(author) )
                matches.add(book);
        }
        return new ArrayList<>(matches);
    }

//-----------------------------------find by author end

    //-----------------------------------find title list

    @Override
    public List<Book> findTitleList(String title) {
        List<Book> matches = new ArrayList<>();
        for (Book book : retrieveAll()){
            if (book.getTitle().equalsIgnoreCase(title))
                matches.add(book);
        }
        return new ArrayList<>(matches);
    }
}
