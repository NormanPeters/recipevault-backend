package com.recipevault.recipevault.user;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, JWTService jwtService, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Registers a new user. The user's password is encrypted before saving.
     *
     * @param user the user to be registered
     * @return the registered user
     */
    public User register(@NonNull User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    /**
     * Verifies a user's login credentials.
     *
     * @param user the user to be verified
     * @return a JWT token if the user is authenticated, or "fail" if the user is not authenticated
     */
    public String verify(@NonNull User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return null;
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved
     * @return an Optional containing the user if found, or empty if not found
     */
    public Optional<User> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their username.
     *
     * @param username the username of the user to be deleted
     * @throws RuntimeException if the user is not found
     */
    @Transactional
    public void deleteByUsername(@NonNull String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User with username: " + username + " not found");
        }
        userRepository.deleteByUsername(username);
    }
}