package com.zhouxiaoxuan.service;

import com.zhouxiaoxuan.entity.Book;
import com.zhouxiaoxuan.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookService {

    @Autowired(required = false)
    private BookMapper bookMapper;

    public List<Book> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    public boolean createBook(String name, String author, String publisher, Date publishDate, String introduction, String coverurl, String url) {
        Book book = new Book(name, author, publisher, publishDate, introduction, coverurl, url);
        return bookMapper.createBook(book);
    }

    public void deleteBook(int id) {
        bookMapper.deleteBook(id);
    }

    public boolean updateBook(int id,String name, String author, String publisher, Date publishDate, String introduction, String coverurl, String url) {
        Book book = new Book(id,name, author, publisher, publishDate, introduction, coverurl, url);
        return bookMapper.updateBook(book);
    }

    public int getIdByName(String name) {
        return bookMapper.getIdByName(name);
    }

    public Book getBookById(Integer id) {
        return bookMapper.getBookById(id);
    }
}
