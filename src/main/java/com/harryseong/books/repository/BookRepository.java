package com.harryseong.books.repository;

import com.harryseong.books.model.Author;
import com.harryseong.books.model.Book;
import com.harryseong.books.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findAllByAuthorsContaining(Author author);
    List<Book> findAllByUsersContaining(User user);
    List<Book> findAll();
    Book findByIsbn10(String isbn10);
    Book findByIsbn13(String isbn13);
    Book findByOtherIdType(String otherIdType);
}
