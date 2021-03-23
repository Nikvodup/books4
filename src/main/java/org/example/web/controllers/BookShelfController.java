package org.example.web.controllers;

import org.apache.log4j.Logger;

import org.example.app.services.BookService;
import org.example.web.dto.AuthorToFind;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.TitleToFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;



import javax.validation.Valid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;


    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;

    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("authorToFind", new AuthorToFind());
        model.addAttribute("titleToFind", new TitleToFind());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }



    @PostMapping("/save")
    public String saveBook(@Valid Book book,  BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
           model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else if(bookService.findAuthors().contains(book.getAuthor())
           && bookService.findTitles().contains(book.getTitle())){
            model.addAttribute("book", book);
            model.addAttribute("message2", "We have the book!");
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());

            return "book_shelf";
        }
         else{

            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";

        }

    }

    @PostMapping("/removeId")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    //------------------looking for author's books

    @GetMapping("/authors")
    public String authors(AuthorToFind authorToFind, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("matches", bookService.findBookList(authorToFind.getAuthor()));
        return "authors_page";
    }


    @ApplicationScope
    @PostMapping("/authors")
    public String findBook(@Valid AuthorToFind authorToFind, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else if (bookService.findBooksByAuthor(authorToFind.getAuthor())) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("line", authorToFind.getLine());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            return     this.authors(authorToFind, model);
        }
    }

    //--------------looking for titles


    @GetMapping("/title")
    public String title(Model model, TitleToFind titleToFind) {
        model.addAttribute("book", new Book());
        model.addAttribute("matches", bookService.findTitleList(titleToFind.getTitle()));
        return "title_page";
    }


    @PostMapping("/title")
    public String findTitle(@Valid TitleToFind titleToFind, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else if (bookService.findBookByTitle(titleToFind.getTitle())) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("news", titleToFind.getNews());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            return this.title(model, titleToFind);
        }
    }

    //-------------------------------upload files
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model)
            throws Exception{

        try {
            String name = file.getOriginalFilename();

            byte[] bytes = file.getBytes();

            // create dir
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "external_uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // create file
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            logger.info("file saved at" + serverFile.getAbsolutePath());

            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("message", "File uploaded successfully.");
            model.addAttribute("bookList", bookService.getAllBooks());

            return "book_shelf";

        } catch (Exception e){
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("authorToFind", new AuthorToFind());
            model.addAttribute("titleToFind", new TitleToFind());
            model.addAttribute("message", "Fail! Or file to upload not found.");
            model.addAttribute("bookList", bookService.getAllBooks());

            return "book_shelf";
        }

    }



}