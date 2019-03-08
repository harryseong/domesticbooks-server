package com.harryseong.books.controller.api.v1;

import com.harryseong.books.BooksApplication;
import com.harryseong.books.domain.Role;

import com.harryseong.books.domain.User;
import com.harryseong.books.dto.UserDTO;
import com.harryseong.books.repository.BookRepository;
import com.harryseong.books.repository.RoleRepository;
import com.harryseong.books.repository.UserRepository;
import com.harryseong.books.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksApplication.class);

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    private Optional<User> getUser(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @PostMapping("")
    private ResponseEntity<String> registerNewUser(@RequestBody @Valid UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role roleUser = roleRepository.findByName("user");
        if (roleUser != null) {
            roles.add(roleUser);
        }

        user.setRoles(roles);
        userRepository.save(user);

        LOGGER.info(String.format("User, %s, has been registered.", user.getEmail()));
        return new ResponseEntity<>(String.format("User, %s, has been registered.", user.getEmail()), HttpStatus.CREATED);
    }
}
