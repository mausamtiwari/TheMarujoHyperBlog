package be.intec.themarujohyperblog.service;


import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user);

    void editUser(Long userId);

    void deleteUser(Long userId);

    Page<User> findUserPaginated(Long userId, int pageNo, int pageSize);
}
