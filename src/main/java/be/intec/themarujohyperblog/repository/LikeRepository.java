package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
