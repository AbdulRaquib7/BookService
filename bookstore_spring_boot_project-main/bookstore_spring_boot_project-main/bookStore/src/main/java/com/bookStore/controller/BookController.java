package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.Book;
import com.bookStore.entity.MyBookList;
import com.bookStore.service.BookService;
import com.bookStore.service.MyBookListService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class BookController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService service;
    
    @Autowired
    private MyBookListService myBookService;
    
    @GetMapping("/")
    public String home() {
        logger.info("Accessing home page");
        return "home";
    }
    
    @GetMapping("/book_register")
    public String bookRegister() {
        logger.info("Accessing book register page");
        return "bookRegister";
    }
    
    @GetMapping("/available_books")
    public ModelAndView getAllBook() {
        logger.info("Fetching all available books");
        List<Book> list = service.getAllBook();
        return new ModelAndView("bookList", "book", list);
    }
    
    @PostMapping("/save")
    public String addBook(@ModelAttribute Book b) {
        logger.info("Saving book: " + b);
        service.save(b);
        return "redirect:/available_books";
    }
    
    @GetMapping("/my_books")
    public String getMyBooks(Model model) {
        logger.info("Fetching my books");
        List<MyBookList> list = myBookService.getAllMyBooks();
        model.addAttribute("book", list);
        return "myBooks";
    }
    
    @PostMapping("/mylist/{id}")
    public String getMyList(@PathVariable("id") int id) {
        logger.info("Adding book with id: " + id + " to my list");
        Book b = service.getBookById(id);
        MyBookList mb = new MyBookList(b.getId(), b.getName(), b.getAuthor(), b.getPrice());
        myBookService.saveMyBooks(mb);
        return "redirect:/my_books";
    }
    
    @GetMapping("/editBook/{id}")
    public String editBook(@PathVariable("id") int id, Model model) {
        logger.info("Editing book with id: " + id);
        Book b = service.getBookById(id);
        model.addAttribute("book", b);
        return "bookEdit";
    }
    
    @DeleteMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        logger.info("Deleting book with id: " + id);
        service.deleteById(id);
        return "redirect:/available_books";
    }
}
