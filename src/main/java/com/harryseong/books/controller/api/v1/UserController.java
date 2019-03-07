package com.harryseong.books.controller.api.v1;

import com.harryseong.books.BooksApplication;
import com.harryseong.books.domain.Role;
import com.harryseong.books.dto.BookDTO;
import com.harryseong.books.domain.Book;
import com.harryseong.books.domain.User;
import com.harryseong.books.dto.UserDTO;
import com.harryseong.books.repository.BookRepository;
import com.harryseong.books.repository.RoleRepository;
import com.harryseong.books.repository.UserRepository;
import com.harryseong.books.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksApplication.class);

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    private Optional<User> getUser(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @PostMapping("")
    private ResponseEntity<String> registerNewUser(@RequestBody @Valid UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role roleUser = roleRepository.findByName("user");
        if (roleUser != null) {
            roles.add(roleUser);
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(String.format("User, %s, has been registered.", user.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("/book")
    public ResponseEntity<String> addBook(@RequestBody @Valid BookDTO bookDTO) {
        Book book = null;
        if (bookDTO.getIsbn10() != null) {
            book = bookRepository.findByIsbn10(bookDTO.getIsbn10());
        }
        if (book == null && bookDTO.getIsbn13() != null) {
            book = bookRepository.findByIsbn13(bookDTO.getIsbn13());
        }
        if (book == null && bookDTO.getOtherIdType() != null) {
            book = bookRepository.findByOtherIdType(bookDTO.getOtherIdType());
        }

        if (book == null) {
            book = bookService.updateBook(new Book(), bookDTO);
            try {
                bookRepository.save(book);
                LOGGER.info("New book saved successfully: {}", book.getTitle());
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to save new book, {}, due to db error: {}", book.getTitle(), e.getMostSpecificCause());
            }
        } else {
            LOGGER.info("Book already exists in db: {}", bookDTO.getTitle());
        }

        // TODO: Replace hardcoded user with actual user.
        User user = userRepository.findById(1).get();
        if (user.getBooks().contains(book)) {
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

    @DeleteMapping("/book")
    private ResponseEntity<String> removeBook(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "bookId") Integer bookId) {
        this.userRepository.findById(userId)
                .ifPresent(user -> this.bookRepository.findById(bookId)
                        .ifPresent(book -> {
                            user.removeBook(book);
                            this.userRepository.save(user);
                        })
                );
        LOGGER.info("Book {} was removed from user {} library. ", bookId, userId);
        return new ResponseEntity<>(String.format("Book, %s, was removed from user %s library.", bookId, userId), HttpStatus.ACCEPTED);
    }
}
