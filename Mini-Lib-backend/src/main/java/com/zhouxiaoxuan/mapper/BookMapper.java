package com.zhouxiaoxuan.mapper;

import com.zhouxiaoxuan.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("SELECT * FROM book")
    List<Book> getAllBooks();

    @Insert("INSERT INTO book (name, author, publisher, publishDate, introduction, coverurl, url) VALUES (#{name}, #{author}, #{publisher}, #{publishDate}, #{introduction}, #{coverurl}, #{url})")
    boolean createBook(Book book);

    @Delete("DELETE FROM book WHERE id = #{id}")
    void deleteBook(int id);

    @Update("UPDATE book SET name = #{name}, author = #{author}, publisher = #{publisher}, publishDate = #{publishDate}, introduction = #{introduction}, coverurl = #{coverurl}, url = #{url} WHERE id = #{id}")
    boolean updateBook(Book book);

    @Select("SELECT id FROM book WHERE name = #{name}")
    int getIdByName(String name);

    @Select("SELECT * FROM book WHERE id = #{id}")
    Book getBookById(Integer id);

    @Select("SELECT * FROM book WHERE name = #{name}")
Book getBookByName(String name);
}
