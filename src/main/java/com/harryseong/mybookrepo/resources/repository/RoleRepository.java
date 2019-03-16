package com.harryseong.mybookrepo.resources.repository;

import com.harryseong.mybookrepo.resources.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
