package com.harryseong.mybookrepo.resources.controller.api.v1;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.domain.Book;
import com.harryseong.mybookrepo.resources.domain.Plan;
import com.harryseong.mybookrepo.resources.domain.PlanBook;
import com.harryseong.mybookrepo.resources.domain.User;
import com.harryseong.mybookrepo.resources.dto.PlanDTO;
import com.harryseong.mybookrepo.resources.repository.BookRepository;
import com.harryseong.mybookrepo.resources.repository.PlanBookRepository;
import com.harryseong.mybookrepo.resources.repository.PlanRepository;
import com.harryseong.mybookrepo.resources.repository.UserRepository;
import com.harryseong.mybookrepo.resources.service.AppUserDetailsService;
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
    PlanBookRepository planBookRepository;

    @Autowired
    AppUserDetailsService userService;
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    private List<Plan> getAllPlans() {
        User user = userService.getAuthenticatedUser();
        return user.getPlans();
    }

    @PutMapping("")
    private ResponseEntity<String> updatePlan(
            @RequestParam(name = "planId") Integer planId,
            @RequestBody @Valid PlanDTO planDTO
    ) {
        Plan plan = this.planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
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
    
    @DeleteMapping("")
    private ResponseEntity<String> deletePlan(
            @RequestParam(name = "planId") Integer planId
    ) {
        User user = userService.getAuthenticatedUser();
        Plan plan = this.planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );
        user.removePlan(plan);

        try {
            userRepository.save(user);
            LOGGER.info("Plan deleted successfully: {}", plan.getName());
            return new ResponseEntity<>(String.format("Plan deleted successfully: %s", plan.getName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to delete plan, {}, due to db error: {}", plan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to delete plan, %s, due to db error.", plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    private ResponseEntity<String> createPlan(
            @RequestBody @Valid PlanDTO planDTO
    ) {
        User user = userService.getAuthenticatedUser();

        Plan newPlan = new Plan();
        newPlan.setName(planDTO.getName());
        newPlan.setDescription(planDTO.getDescription());
        newPlan.setUser(user);

        LOGGER.info("{}", user);
        LOGGER.info("{}", newPlan);

        try {
            this.planRepository.save(newPlan);
            LOGGER.info("New plan, {}, saved successfully for user, {}.", newPlan.getName(), user.getUsername());
            return new ResponseEntity<>(String.format("New plan, %s, saved successfully for user, %s.", newPlan.getName(), user.getUsername()), HttpStatus.CREATED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to save new plan, {}, for user, {}, due to db error: {}.", newPlan.getName(), user.getUsername(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to save new plan, %s, for user, %s, due to db error.",  newPlan.getName(), user.getUsername()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/book")
    private List<PlanBook> getAllPlanBooks(
            @RequestParam(name = "planId") Integer planId
    ) {
        Plan plan = planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );
        return planBookRepository.findByPlan(plan);
    }

    @PostMapping("/book")
    public ResponseEntity<String> addBookToPlan(
            @RequestParam(name = "planId") Integer planId,
            @RequestParam(name = "bookId") Integer bookId
    ) {
        Plan plan = planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Book not found with id: %s.", bookId))
        );
        
        // Primary: Check if book already exists in plan. If not, add book to plan.
        if (planBookRepository.findByPlanAndBook(plan, book) != null) {
            LOGGER.info("Book, {}, already in plan, {}.", book.getTitle(), plan.getName());
            return new ResponseEntity<>(String.format("Book, %s, already in %s's plan.", book.getTitle(), plan.getName()), HttpStatus.CONFLICT);
        } else {
            plan.addBook(book, 0);

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

    @PutMapping("/book")
    public ResponseEntity<String> updateBookInPlan(
            @RequestParam(name = "planId") Integer planId,
            @RequestParam(name = "bookId") Integer bookId,
            @RequestParam(name = "newStatus") Integer newStatus
    ) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Book not found with id: %s.", bookId))
        );

        PlanBook planBook = planBookRepository.findByPlanAndBook(plan, book);
        planBook.setStatus(newStatus);

        try {
            planBookRepository.save(planBook);
            LOGGER.info("Book, {}, status updated to {} in plan, {}.", planBook.getBook().getTitle(), newStatus, planBook.getPlan().getName());
            return new ResponseEntity<>(String.format("Book, %s, status updated to %s in plan, %s.", planBook.getBook().getTitle(), newStatus, planBook.getPlan().getName()), HttpStatus.CREATED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to update book, {}, status in plan, {}, due to db error: {}.", book.getTitle(), plan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to update book, %s, status in plan, %s, due to db error.", book.getTitle(), plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/book")
    private ResponseEntity<String> removeBookFromPlan(
            @RequestParam(name = "planId") Integer planId,
            @RequestParam(name = "bookId") Integer bookId
    ) {
        Plan plan = planRepository.findById(planId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Plan not found with id: %s.", planId))
        );

        Book book = bookRepository.findById(bookId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Book not found with id: %s.", bookId))
        );

        plan.removeBook(book);
        try {
            this.planRepository.save(plan);
            LOGGER.info("Book, {}, removed from plan, {}.", book.getTitle(), plan.getName());
            return new ResponseEntity<>(String.format("Book, %s, removed from plan, %s.", book.getTitle(), plan.getName()), HttpStatus.ACCEPTED);
        } catch (UnexpectedRollbackException e) {
            LOGGER.error("Unable to remove book, {}, from plan, {}, due to db error: {}.", book.getTitle(), plan.getName(), e.getMostSpecificCause());
            return new ResponseEntity<>(String.format("Unable to remove book, %s, from plan, %s, due to db error.", book.getTitle(), plan.getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
