package com.harryseong.mybookrepo.resources.controller.api.v1;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.domain.Plan;
import com.harryseong.mybookrepo.resources.dto.PlanDTO;
import com.harryseong.mybookrepo.resources.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/plan")
public class PlanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesApplication.class);

    @Autowired
    PlanRepository planRepository;

    @GetMapping("/{id}")
    private Plan getPlan(@PathVariable Integer id) {
        return planRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", id))
        );
    }

    @PutMapping("/{id}")
    private ResponseEntity<String> savePlan(@PathVariable Integer id, @RequestBody @Valid PlanDTO planDTO) {
        Plan plan = this.planRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", id))
        );
        plan.setName(planDTO.getName());
        plan.setDescription(planDTO.getDescription());

        try {
            planRepository.save(plan);
            LOGGER.info("Plan updated successfully: {}", plan.getName());
            return new ResponseEntity<>(String.format("Plan updated successfully: %s", plan.getName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to update plan, {}, due to db error: {}", plan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to update plan, %s", plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    private ResponseEntity<String> deletePlan(@PathVariable Integer id) {
        Plan plan = this.planRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", id))
        );

        try {
            planRepository.delete(plan);
            LOGGER.info("Plan deleted successfully: {}", plan.getName());
            return new ResponseEntity<>(String.format("Plan deleted successfully: %s", plan.getName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to delete plan, {}, due to db error: {}", plan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to delete plan, %s", plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    private ResponseEntity<String> savePlan(@RequestBody @Valid PlanDTO planDTO) {
        Plan newPlan = new Plan();
        newPlan.setName(planDTO.getName());
        newPlan.setDescription(planDTO.getDescription());

        try {
            this.planRepository.save(newPlan);
            LOGGER.info("New plan saved successfully: {}", newPlan.getName());
            return new ResponseEntity<>(String.format("New plan saved successfully: %s",  newPlan.getName()), HttpStatus.CREATED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to save new plan, {}, due to db error: {}",  newPlan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to save new plan, %s",  newPlan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
