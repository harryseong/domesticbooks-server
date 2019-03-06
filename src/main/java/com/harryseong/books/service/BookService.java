package com.harryseong.books.service;

import com.harryseong.books.BooksApplication;
import com.harryseong.books.dto.AuthorDTO;
import com.harryseong.books.dto.BookDTO;
import com.harryseong.books.dto.CategoryDTO;
import com.harryseong.books.domain.Author;
import com.harryseong.books.domain.Book;
import com.harryseong.books.domain.Category;
import com.harryseong.books.repository.AuthorRepository;
import com.harryseong.books.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksApplication.class);

    @Autowired
    AuthorRepository authorRepository;

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
}
