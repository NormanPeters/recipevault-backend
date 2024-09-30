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

import java.util.List;
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
     * Verifies a user's login credentials and sets a JWT as an HttpOnly cookie.
     *
     * @param user the user to be verified
     * @return a ResponseEntity with the JWT in an HttpOnly cookie, or "fail" if the user is not authenticated
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // Authentifizieren des Benutzers und JWT erzeugen
        String jwt = userService.verify(user);

        if (jwt == null) {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }

        // JWT in einem HttpOnly-Cookie speichern
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)     // Verhindert den Zugriff per JavaScript (sicher vor XSS)
                .secure(true)       // Nur über HTTPS übertragbar (true)
                .path("/")          // Gilt für die gesamte Domain
                .maxAge(24 * 60 * 60) // 24 Stunden gültig
                .sameSite("Strict")  // Verhindert, dass das Cookie in fremden Kontexts gesendet wird (CSRF-Schutz)
                .build();

        // JWT als Cookie im Header der Response senden
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("Login successful");
    }

    /**
     * Logs out a user by clearing the JWT cookie.
     *
     * @param response the HttpServletResponse
     * @return a ResponseEntity with a message indicating the logout was successful
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear the JWT cookie on the server-side
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)  // Expire immediately
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successful");
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


