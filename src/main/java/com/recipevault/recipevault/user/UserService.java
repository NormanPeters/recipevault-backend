package com.recipevault.recipevault.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    AuthenticationManager authManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user. The user's password is encrypted before saving.
     *
     * @param user the user to be registered
     * @return the registered user
     */
    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken()  ;
        } else {
            return "fail";
        }
    }

    /**
     * Creates a new user. If the username already exists, throws a RuntimeException.
     * The user's password is encrypted before saving.
     *
     * @param user the user to be created
     * @return the created user
     */
    public Users createUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists.");
        }
        user.setPassword
                //(encryptPassword
                        (user.getPassword());
        return userRepository.save(user);
    }

    /**
     * Encrypts the given password using BCrypt.
     *
     * @param password the password to be encrypted
     * @return the encrypted password
     */
    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved
     * @return an Optional containing the user if found, or empty if not found
     */
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}