package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(Long userId);
    Optional<User> findByUsernameAndPassword(String username, String password);


    //long count();
}

