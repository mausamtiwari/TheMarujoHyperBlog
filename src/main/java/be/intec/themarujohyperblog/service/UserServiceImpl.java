package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.controller.UserController;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void registerUser(User user) {
       /* String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.debug("Raw password during registration: {}", rawPassword);
        logger.debug("Encoded password during registration: {}", encodedPassword);
        user.setPassword(encodedPassword);*/
        user.setEnabled(true);
        userRepository.save(user);
    }


    @Override
    public User save(User user) {
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserById(Long userid) {
        return userRepository.findUserById(userid);
    }

    @Override
    @Validated(User.UpdateGroup.class)
    public void updateUser(@Validated(User.UpdateGroup.class) User user) {
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserNameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
