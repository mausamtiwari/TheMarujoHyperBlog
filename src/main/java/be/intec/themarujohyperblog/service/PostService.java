package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<BlogPost> getAllPosts();

    BlogPost getPostById(Long blogPostId);

    void savePost(BlogPost blogPost);

    void deletePost(Long id);

    Page<BlogPost> findPostPaginated(int pageNo, int pageSize);

    List<BlogPost> getPostsByUser(User user); // Add this method

    Page<BlogPost> findUserPostsPaginated(User user, int pageNo, int pageSize);


}
