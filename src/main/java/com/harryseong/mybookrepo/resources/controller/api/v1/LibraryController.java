package com.harryseong.mybookrepo.resources.controller.api.v1;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.domain.Book;
import com.harryseong.mybookrepo.resources.domain.User;
import com.harryseong.mybookrepo.resources.domain.UserBook;
import com.harryseong.mybookrepo.resources.dto.BookDTO;
import com.harryseong.mybookrepo.resources.repository.BookRepository;
import com.harryseong.mybookrepo.resources.repository.RoleRepository;
import com.harryseong.mybookrepo.resources.repository.UserRepository;
import com.harryseong.mybookrepo.resources.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/library")
public class LibraryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesApplication.class);

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        LOGGER.info("Fetching all books for {}.", auth.getName());

        List<Book> books = new ArrayList<>();
        List<UserBook> userBooks = user.getBooks();
        userBooks.forEach(userBook -> books.add(userBook.getBook()));
        LOGGER.info(books.toString());
        return books;
    }

    @PostMapping("/book")
    public ResponseEntity<String> addBook(@RequestBody @Valid BookDTO bookDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        // Check if book exists in DB by any of the book identification info.
        Book book = bookService.findBookByIdentifiers(bookDTO);

        LOGGER.info("Add book, '{}', to {}'s library.", bookDTO.getTitle(), auth.getName());

        // Check if book already exists in user's library. If not, add book to user library.
        if (user.hasBook(book)) {
            LOGGER.info("Book, {}, already in {}'s library.", book.getTitle(), user.getFullName());
            return new ResponseEntity<>(String.format("Book, %s, already in %s's library.", book.getTitle(), user.getFullName()), HttpStatus.ACCEPTED);
        } else {
            user.addBook(book);

            try {
                userRepository.save(user);
                LOGGER.info("Book, {}, added to {}'s library.", book.getTitle(), user.getFullName());
                return new ResponseEntity<>(String.format("New book saved successfully: %s.", book.getTitle()), HttpStatus.CREATED);
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to add book, {}, to {}'s library due to db error: {}.", book.getTitle(), user.getFullName(), e.getMostSpecificCause());
                return new ResponseEntity<>(String.format("Unable to add book, %s, to %s's library due to db error.", book.getTitle(), user.getFullName()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/book/{bookId}")
    private ResponseEntity<String> removeBook(@PathVariable String bookId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        Book book = bookRepository.findById(Integer.parseInt(bookId)).orElseThrow(
                () -> new EntityNotFoundException(String.format("Book not found with id: %s.", bookId))
        );

        user.removeBook(book);

        try {
            this.userRepository.save(user);
            LOGGER.info("'{}' was removed from {}'s library. ", book.getTitle(), user.getFullName());
            return new ResponseEntity<>(String.format("'%s' was removed from %s's library.", book.getTitle(), user.getFullName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.info("Unable to remove '{}' from {}'s library due to db error. ", book.getTitle(), user.getFullName());
            return new ResponseEntity<>(String.format("Unable  to remove '%s' from %s's library due to db error.", book.getTitle(), user.getFullName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
