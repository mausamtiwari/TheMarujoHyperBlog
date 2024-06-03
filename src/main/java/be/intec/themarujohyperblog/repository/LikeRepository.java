package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(BlogPost post, User user);

    int countByPost(BlogPost post);
}
