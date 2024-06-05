package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;



    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

 /*  @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }*/

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
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public User updateUser(@Validated(User.UpdateGroup.class) User user) {
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserNameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public long countUsers() {
        return (int) userRepository.count();
    }

}
