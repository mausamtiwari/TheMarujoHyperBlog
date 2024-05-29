package be.intec.themarujohyperblog.repository;


import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByUser(User user);

    Page<BlogPost> findByUser(User user, Pageable pageable);
}
