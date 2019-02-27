package com.harryseong.books.controller.api.v1;

import com.harryseong.books.BooksApplication;
import com.harryseong.books.dto.AuthorDTO;
import com.harryseong.books.model.Author;
import com.harryseong.books.model.Book;
import com.harryseong.books.repository.AuthorRepository;
import com.harryseong.books.repository.BookRepository;
import com.harryseong.books.service.AuthorService;
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
@RequestMapping(value = "/api/v1/author")
public class AuthorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksApplication.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("")
    private List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    private Author getAuthor(@PathVariable Integer id) {
        return authorRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Author not found with id: %s.", id))
        );
    }

    @PutMapping("/{id}")
    private ResponseEntity updateAuthor(@PathVariable Integer id, @RequestBody @Valid AuthorDTO authorDTO) {
        Author author = this.authorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Author not found with id: %s.", id))
        );
        author = authorService.updateAuthor(author, authorDTO);
        try {
            authorRepository.save(author);
            LOGGER.info("Author updated successfully: {}", author.getFullName());
            return new ResponseEntity<>(String.format("Author updated successfully: %s", author.getFullName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to update author, {}, due to db error: {}", author.getFullName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to update author, %s", author.getFullName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    private ResponseEntity<String> saveAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        Author newAuthor = new Author();
        newAuthor = authorService.updateAuthor(newAuthor, authorDTO);

        Author foundAuthor = authorRepository.findByFirstNameAndMiddleNameAndLastName(authorDTO.getFirstName(),
                authorDTO.getMiddleName(),
                authorDTO.getLastName());
        if (null!=foundAuthor) {
            return new ResponseEntity<>(String.format("Author already exists in db: %s", newAuthor.getFullName()), HttpStatus.ACCEPTED);
        }

        try {
            authorRepository.save(newAuthor);
            LOGGER.info("New author saved successfully: {}", newAuthor.getFullName());
            return new ResponseEntity<>(String.format("New author saved successfully: %s", newAuthor.getFullName()), HttpStatus.CREATED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to save new author, {}, due to db error: {}", newAuthor.getFullName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to new save author, %s", newAuthor.getFullName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/books")
    private List<Book> getAuthorBooks(@PathVariable Integer id) {
        Optional<Author> a = authorRepository.findById(id);
        if (a.isPresent()) {
            Author author = a.get();
            LOGGER.info("Returning list of books for author: {}", author.getFullName());

            List<Book> books = bookRepository.findAllByAuthorsContaining(author);
            if (books.isEmpty()) {
                LOGGER.info("Books have been found by {}: {}", author.getFullName(), books);
            } else {
                LOGGER.info("No books have been found by {}", author.getFullName());
            }

            return books;
        } else {
            LOGGER.info("The author does not exist in the database. Author ID: {}", id);
            return new ArrayList<>();
        }
    }

}
