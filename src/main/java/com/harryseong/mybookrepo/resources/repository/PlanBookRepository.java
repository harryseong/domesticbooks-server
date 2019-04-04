package com.harryseong.mybookrepo.resources.repository;

import com.harryseong.mybookrepo.resources.domain.Plan;
import com.harryseong.mybookrepo.resources.domain.PlanBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanBookRepository extends CrudRepository<PlanBook, Integer> {
    List<PlanBook> findByPlan(Plan plan);
}
