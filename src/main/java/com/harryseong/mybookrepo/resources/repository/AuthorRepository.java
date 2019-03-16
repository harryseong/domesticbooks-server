package com.harryseong.mybookrepo.resources.repository;

import com.harryseong.mybookrepo.resources.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Author findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    List<Author> findAll();
}
