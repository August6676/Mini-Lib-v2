package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.entity.Book;
import com.zhouxiaoxuan.mapper.BookMapper;
import com.zhouxiaoxuan.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/books")
@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
public class Admin_BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        System.out.println(bookService.getAllBooks());
        return bookService.getAllBooks();

    }

    @PostMapping
    public Book createBook(
            @RequestParam String name,
            @RequestParam String author,
            @RequestParam String publisher,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date publishDate,
            @RequestParam String introduction,
            @RequestParam String coverurl,
            @RequestParam String url) {
        try {
            boolean isCreated = bookService.createBook(name, author, publisher, publishDate, introduction, coverurl, url);
            if (isCreated) {
                return bookMapper.getBookByName(name);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("删除失败");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(
            @PathVariable int id,
            @RequestBody Book updatedBook) {
        try {
            boolean isUpdated = bookService.updateBook(id, updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getPublisher(), updatedBook.getPublishDate(), updatedBook.getIntroduction(), updatedBook.getCoverurl(), updatedBook.getUrl());
            if (isUpdated) {
                return ResponseEntity.ok("书籍更新成功");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到相应的书籍");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误");
        }
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

}
