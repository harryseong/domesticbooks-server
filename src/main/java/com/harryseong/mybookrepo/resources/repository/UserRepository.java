package com.harryseong.mybookrepo.resources.repository;

import com.harryseong.mybookrepo.resources.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
