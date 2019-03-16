package com.harryseong.mybookrepo.resources.service;

import com.harryseong.mybookrepo.resources.dto.AuthorDTO;
import com.harryseong.mybookrepo.resources.domain.Author;
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
