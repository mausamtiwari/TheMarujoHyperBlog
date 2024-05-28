package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
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
        if (user.getId() != null) {
            // Editing an existing employee
            Optional<User> existingUserOptional = userRepository.findById(user.getId());
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                if (!existingUser.getEmail().equals(user.getEmail())) {
                    // Check if the new email is taken by another employee
                    Optional<User> emailCheckEmployee = userRepository.findByEmail(user.getEmail());
                    if (emailCheckEmployee.isPresent()) {
                        throw new IllegalStateException("email taken");
                    }
                }
                // Save the updated employee details
                userRepository.save(user);
            } else {
                throw new IllegalStateException("employee not found");
            }
        } else {
            // Creating a new employee
            Optional<User> optionalEmployee = userRepository.findByEmail(user.getEmail());
            if (optionalEmployee.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            userRepository.save(user);
        }
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
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserNameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

   /* @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }*/
}
