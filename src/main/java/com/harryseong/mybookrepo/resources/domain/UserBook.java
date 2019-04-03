package com.harryseong.mybookrepo.resources.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_book")
public class UserBook {

    @EmbeddedId
    private UserBook_PK id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("bookId")
    private Book book;

    private Boolean haveRead;

    private Date haveReadDate;

    public UserBook() {
    }

    public UserBook(User user, Book book) {
        this.user = user;
        this.book = book;
        this.id = new UserBook_PK(user.getId(), book.getId());
    }

    public UserBook_PK getId() {
        return id;
    }

    public void setId(UserBook_PK id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Boolean getHaveRead() {
        return haveRead;
    }

    public void setHaveRead(Boolean haveRead) {
        this.haveRead = haveRead;
    }

    public Date getHaveReadDate() {
        return haveReadDate;
    }

    public void setHaveReadDate(Date haveReadDate) {
        this.haveReadDate = haveReadDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserBook that = (UserBook) o;
        return Objects.equals(user, that.user) &&
            Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, book);
    }
}
