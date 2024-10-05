package com.recipevault.recipevault.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * Verifies a user's login credentials.
     *
     * @param user the user to be verified
     * @return a JWT token if the user is authenticated, or null if the user is not authenticated
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        String token = userService.verify(user);

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved
     * @return a ResponseEntity containing the user and HTTP status OK, or NOT_FOUND if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Deletes a user by their username.
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        try {
            userService.deleteByUsername(username);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}


