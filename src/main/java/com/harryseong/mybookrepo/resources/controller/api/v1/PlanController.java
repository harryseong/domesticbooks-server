package com.harryseong.mybookrepo.resources.controller.api.v1;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.domain.Book;
import com.harryseong.mybookrepo.resources.domain.Plan;
import com.harryseong.mybookrepo.resources.domain.User;
import com.harryseong.mybookrepo.resources.dto.BookDTO;
import com.harryseong.mybookrepo.resources.dto.PlanDTO;
import com.harryseong.mybookrepo.resources.repository.BookRepository;
import com.harryseong.mybookrepo.resources.repository.PlanRepository;
import com.harryseong.mybookrepo.resources.repository.UserRepository;
import com.harryseong.mybookrepo.resources.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/plan")
public class PlanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesApplication.class);

    @Autowired
    BookRepository bookRepository;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    PlanRepository planRepository;
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    private Plan getPlan(@PathVariable Integer id) {
        return planRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", id))
        );
    }

    @PutMapping("/{id}")
    private ResponseEntity<String> updatePlan(@PathVariable Integer id, @RequestBody @Valid PlanDTO planDTO) {
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


    @GetMapping("/books")
    public List<Book> getAllBooksFromPlan(@RequestParam(name = "planId") Integer planId) {
        Plan plan = planRepository.findById(planId).get();
        return bookRepository.findAllByPlansContaining(plan);
    }

    @PostMapping("/book")
    public ResponseEntity<String> addBookToPlan(@RequestBody @Valid BookDTO bookDTO, @RequestParam(name = "userId") Integer userId, @RequestParam(name = "planId") Integer planId) {

        // Check if book exists in DB by any of the book identification info.
        Book book = bookService.findBookByIdentifiers(bookDTO);

        User user = userRepository.findById(userId).get();
        Plan plan = planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );

        // Secondary: Check if book already exists in user's library. If not, add book to user library.
        if (user.getBooks().contains(book)) {
            LOGGER.info("Book, {}, already in {}'s library.", book.getTitle(), user.getFullName());
        } else {
            user.addBook(book);

            try {
                userRepository.save(user);
                LOGGER.info("Book, {}, added to {}'s library.", book.getTitle(), user.getFullName());
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to add book, {}, to {}'s library due to db error: {}.", book.getTitle(), user.getFullName(), e.getMostSpecificCause());
                return new ResponseEntity<>(String.format("Unable to add book, %s, to %s's library due to db error.", book.getTitle(), user.getFullName()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        
        // Primary: Check if book already exists in plan. If not, add book to plan.
        if (plan.getBooks().contains(book)) {
            LOGGER.info("Book, {}, already in plan, {}.", book.getTitle(), plan.getName());
            return new ResponseEntity<>(String.format("Book, %s, already in %s's plan.", book.getTitle(), plan.getName()), HttpStatus.ACCEPTED);
        } else {
            plan.addBook(book);

            try {
                planRepository.save(plan);
                LOGGER.info("Book, {}, added to plan, {}.", book.getTitle(), plan.getName());
                return new ResponseEntity<>(String.format("Book, %s, added to plan, %s.", book.getTitle(), plan.getName()), HttpStatus.CREATED);
            } catch (UnexpectedRollbackException e) {
                LOGGER.error("Unable to add book, {}, to plan, {}, due to db error: {}.", book.getTitle(), plan.getName(), e.getMostSpecificCause());
                return new ResponseEntity<>(String.format("Unable to add book, %s, to plan, %s, due to db error.", book.getTitle(), plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/book")
    private ResponseEntity<String> removeBookFromPlan(@RequestParam(name = "planId") Integer planId, @RequestParam(name = "bookId") Integer bookId) {
        this.planRepository.findById(planId)
            .ifPresent(plan -> this.bookRepository.findById(bookId)
                .ifPresent(book -> {
                    plan.removeBook(book);
                    this.planRepository.save(plan);
                })
            );
        LOGGER.info("Book {} was removed from plan, {}. ", bookId, planId);
        return new ResponseEntity<>(String.format("Book, %s, was removed from plan, %s.", bookId, planId), HttpStatus.ACCEPTED);
    }
}
