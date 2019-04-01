package com.harryseong.mybookrepo.resources.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserBook_PK implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Integer bookId;

    public UserBook_PK() {
    }

    public UserBook_PK(Integer userId, Integer bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserBook_PK that = (UserBook_PK) o;
        return Objects.equals(userId, that.userId) &&
            Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
