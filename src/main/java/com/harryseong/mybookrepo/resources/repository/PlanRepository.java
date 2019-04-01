package com.harryseong.mybookrepo.resources.repository;

import com.harryseong.mybookrepo.resources.domain.Plan;
import com.harryseong.mybookrepo.resources.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
    List<Plan> findByUser(User user);
}
