package com.recipevault.recipevault.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user. If the username already exists, throws a RuntimeException.
     * The user's password is encrypted before saving.
     *
     * @param user the user to be created
     * @return the created user
     */
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists.");
        }
        user.setPassword
                (encryptPassword
        (user.getPassword()));
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
    public Optional<User> getUserById(Long id) {
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
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
