package be.intec.themarujohyperblog.service;


import be.intec.themarujohyperblog.model.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    void registerUser(User user);

    public User updateUser(User user);

    void deleteUser(Long userId);

    Optional<User> findByUserName(String username);

    Optional<User> findUserById(Long userid);
    Optional<User> findUserByEmail(String email);

    Optional<User> findByUserNameAndPassword(String username, String password);

    // UserDetails loadUserByUsername(String username);




}
