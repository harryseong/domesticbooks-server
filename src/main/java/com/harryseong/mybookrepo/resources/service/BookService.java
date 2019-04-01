package com.harryseong.mybookrepo.resources.service;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.dto.AuthorDTO;
import com.harryseong.mybookrepo.resources.dto.BookDTO;
import com.harryseong.mybookrepo.resources.dto.CategoryDTO;
import com.harryseong.mybookrepo.resources.domain.Author;
import com.harryseong.mybookrepo.resources.domain.Book;
import com.harryseong.mybookrepo.resources.domain.Category;
import com.harryseong.mybookrepo.resources.repository.AuthorRepository;
import com.harryseong.mybookrepo.resources.repository.BookRepository;
import com.harryseong.mybookrepo.resources.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesApplication.class);

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Author> updateBookAuthors(AuthorDTO[] authorDTOs) {
        List<Author> authors = new ArrayList<>();
        for (AuthorDTO authorDTO:authorDTOs) {
            String firstName = authorDTO.getFirstName();
            String middleName = authorDTO.getMiddleName();
            String lastName = authorDTO.getLastName();
            Author author = authorRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);

            if (null==author) {
                LOGGER.info("Author not found in db: {} {} {}", firstName, middleName, lastName);
                author = new Author(firstName, middleName, lastName);
                authorRepository.save(author);
                LOGGER.info("New author saved successfully: {} {} {}", firstName, middleName, lastName);
            } else {
                LOGGER.info("Author found in db: {} {} {}", firstName, middleName, lastName);
            }

            authors.add(author);
        }
        return authors;
    }

    public List<Category> updateBookCategories(CategoryDTO[] categoryDTOs) {
        List<Category> categories = new ArrayList<>();
        for (CategoryDTO categoryDTO:categoryDTOs) {
            String name = categoryDTO.getName();
            Category category = categoryRepository.findByName(name);

            if (null==category) {
                LOGGER.info("Category not found in db: {}", name);
                category = new Category(name);
                categoryRepository.save(category);
                LOGGER.info("New category saved successfully: {}", name);
            } else {
                LOGGER.info("Category found in db: {}", name);
            }

            categories.add(category);
        }
        return categories;
    }

    public Book updateBook(Book book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setCoverImageURL(bookDTO.getCoverImageURL());
        book.setDescription(bookDTO.getDescription());
        book.setPrintType(bookDTO.getPrintType());
        book.setIsbn10(bookDTO.getIsbn10());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setOtherIdType(bookDTO.getOtherIdType());
        book.setPageCount(bookDTO.getPageCount());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());

        // Build list of authors to add to new book. If an author does not already exist, save as new author into the db.
        if (bookDTO.getAuthors() != null && bookDTO.getAuthors().length != 0) {
            try {
                List<Author> authors = new ArrayList<>(updateBookAuthors(bookDTO.getAuthors()));
                book.setAuthors(authors);
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to save author due to db error: {}", e.getMostSpecificCause());
            }
        } else {
            LOGGER.info("Book does not have listed authors: {}", bookDTO.getIsbn13());
        }

        // Build list of categories to add to new book. If an category does not already exist, save as new category into the db.
        if (bookDTO.getCategories() != null) {
            try {
                List<Category> categories = new ArrayList<>(updateBookCategories(bookDTO.getCategories()));
                book.setCategories(categories);
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to save category due to db error: {}", e.getMostSpecificCause());
            }
        } else {
            LOGGER.info("Book does not have listed categories: {}", bookDTO.getIsbn13());
        }
        return book;
    }

    public Book findBookByIdentifiers(BookDTO bookDTO) {
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

        // If book does not yet exist in DB, save as new book.
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

        return book;
    }
}
