package com.harryseong.books.controller.api.v1;

import com.harryseong.books.BooksApplication;
import com.harryseong.books.dto.BookDTO;
import com.harryseong.books.model.Author;
import com.harryseong.books.model.Book;
import com.harryseong.books.model.Category;
import com.harryseong.books.model.User;
import com.harryseong.books.repository.AuthorRepository;
import com.harryseong.books.repository.BookRepository;
import com.harryseong.books.repository.UserRepository;
import com.harryseong.books.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksApplication.class);

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public List<Book> getAllBooks() {

        // TODO: Replace hardcoded user with actual user.
        User user = userRepository.findById(1).get();
        return bookRepository.findAllByUsersContaining(user);
    }

    @GetMapping("/{id}")
    public Optional<Book> getBook(@PathVariable Integer id) {
        return bookRepository.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Integer id, @RequestBody @Valid BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Book not found with id: %s.", id))
        );
        book = bookService.updateBook(book, bookDTO);

        // Build list of authors to add to new book. If an author does not already exist, save as new author into the db.
        List<Author> authors = new ArrayList<>();
        try {
            bookService.updateBookAuthors(bookDTO.getAuthors());
            book.setAuthors(authors);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to save author due to db error: {}", e.getMostSpecificCause());
        }

        try {
            bookRepository.save(book);
            LOGGER.info("Book updated successfully: {}", book.getTitle());
            return new ResponseEntity<>(String.format("Book updated successfully: %s", book.getTitle()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to update book, {}, due to db error: {}", book.getTitle(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to update book, %s", book.getTitle()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
