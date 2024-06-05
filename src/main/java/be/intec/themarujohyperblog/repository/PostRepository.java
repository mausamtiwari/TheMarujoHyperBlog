package be.intec.themarujohyperblog.repository;


import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository; //Mausam??
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<BlogPost, Long> {
    
        long count();
        List<BlogPost> findByUser(User user);

        Page<BlogPost> findByUser(User user, Pageable pageable);


        List<BlogPost> findByDescriptionContaining(String search);

        List<BlogPost> findByContentContaining(String search);

        List<BlogPost> findByTitleContaining(String search);


}


