package com.recipevault.recipevault.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves the CSRF token.
     *
     * @param token the HttpServletRequest
     * @return the CSRF token
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest token) {
        return (CsrfToken) token.getAttribute("_csrf");
    }

    /**
     * Registers a new user. The user's password is encrypted before saving.
     *
     * @param user the user to be registered
     * @return the registered user
     */
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);

    }

    /**
     * Verifies a user's login credentials.
     *
     * @param user the user to be verified
     * @return a JWT token if the user is authenticated, or "fail" if the user is not authenticated
     */
    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }

    /**
     * Creates a new user. If the username already exists, returns a BAD_REQUEST response.
     *
     * @param user the user to be created
     * @return a ResponseEntity containing the created user and HTTP status CREATED, or BAD_REQUEST if the username already exists
     */
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            Users createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved
     * @return a ResponseEntity containing the user and HTTP status OK, or NOT_FOUND if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Deletes a user by their ID.
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}

