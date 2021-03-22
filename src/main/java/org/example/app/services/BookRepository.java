package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.AuthorToFind;
import org.example.web.dto.Book;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.ApplicationScope;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT*FROM books",(ResultSet rs, int rowNum)->{
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        if(book.getAuthor()==null & book.getTitle().length() !=0 & book.getSize()==null) {
            book.setId(null);
        } else {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("author", book.getAuthor());
            parameterSource.addValue("title", book.getTitle());
            parameterSource.addValue("size", book.getSize());
            jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES(:author,:title,:size)", parameterSource);
            logger.info("store new book: " + book);
        }
    }

    @Override
    public boolean removeItemById(Integer id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
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

    public List<String> findTitles(){
        List<String> titles = new ArrayList<>();
        for (Book book : retrieveAll()){
            titles.add(book.getTitle());
        }
        return new ArrayList(titles);
    }

    public List<String> findAuthors(){
        List<String> authors = new ArrayList<>();
        for (Book book : retrieveAll()){
            authors.add(book.getAuthor());
        }
        return new ArrayList(authors);
    }
}