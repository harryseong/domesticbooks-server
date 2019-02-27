package com.harryseong.books.repository;

import com.harryseong.books.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findByName(String name);
}
