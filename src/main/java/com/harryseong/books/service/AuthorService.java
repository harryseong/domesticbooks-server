package com.harryseong.books.service;

import com.harryseong.books.dto.AuthorDTO;
import com.harryseong.books.domain.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    public Author updateAuthor(Author author, AuthorDTO authorDTO) {
        author.setFirstName(authorDTO.getFirstName());
        author.setMiddleName(authorDTO.getMiddleName());
        author.setLastName(authorDTO.getLastName());
        return author;
    }
}
