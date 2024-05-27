package com.zhouxiaoxuan.entity;

import java.util.Date;

public class Book {
    private Integer id;
    private String name;
    private String author;
    private String publisher;
    private Date publishDate;
    private String introduction;
    private String Coverurl; // 书籍的封面地址
    private String url; // 书籍的地址

    public Book() {
    }

    public Book(int id, String name, String author, String publisher, Date publishDate, String introduction, String Coverurl, String url) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.introduction = introduction;
        this.Coverurl = Coverurl;
        this.url = url;
    }

    public Book(String name, String author, String publisher, Date publishDate, String introduction, String coverurl, String url) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.introduction = introduction;
        Coverurl = coverurl;
        this.url = url;
    }

    public String getCoverurl() {
        return Coverurl;
    }

    public void setCoverurl(String coverurl) {
        Coverurl = coverurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
