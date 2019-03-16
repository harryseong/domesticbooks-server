package com.harryseong.mybookrepo.resources.controller.api.v1;

import com.harryseong.mybookrepo.resources.ResourcesApplication;
import com.harryseong.mybookrepo.resources.domain.Role;

import com.harryseong.mybookrepo.resources.domain.User;
import com.harryseong.mybookrepo.resources.dto.UserDTO;
import com.harryseong.mybookrepo.resources.repository.BookRepository;
import com.harryseong.mybookrepo.resources.repository.RoleRepository;
import com.harryseong.mybookrepo.resources.repository.UserRepository;
import com.harryseong.mybookrepo.resources.service.BookService;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesApplication.class);

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

    @GetMapping("/id/{id}")
    private Optional<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @GetMapping("/email/{email}")
    private User getUserByName(@PathVariable String email) { return userRepository.findByEmail(email); }

    @PostMapping("")
    private ResponseEntity<String> registerNewUser(@RequestBody @Valid UserDTO userDTO) {

        // If user already exists,
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            LOGGER.warn(String.format("User, %s, already exists in the system.", userDTO.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, already exists in the system.", userDTO.getEmail()), HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role roleUser = roleRepository.findByName("user");
        if (roleUser != null) {
            roles.add(roleUser);
        }

        user.setRoles(roles);

        try {
            userRepository.save(user);
            LOGGER.info(String.format("User, %s, has been registered.", user.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, has been registered.", user.getEmail()), HttpStatus.CREATED);
        } catch (Error e) {
            LOGGER.error(String.format("User, %s, could not be registered due to a server error.", user.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, could not be registered due to a server error.", user.getEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
