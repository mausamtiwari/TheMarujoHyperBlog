package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.OnlineUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnlineUserRepository extends JpaRepository<OnlineUser, Long> {
    long count();
    Optional<OnlineUser> findByUsername(String username);
    Optional<OnlineUser> getOnlineUserById(Long userId);
}


