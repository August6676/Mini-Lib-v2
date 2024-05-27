package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.entity.Book;
import com.zhouxiaoxuan.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
@RequestMapping("/user/book")
public class User_BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        System.out.println(bookService.getAllBooks());
        System.out.println("User_BookController.getAllBooks");
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }
}
