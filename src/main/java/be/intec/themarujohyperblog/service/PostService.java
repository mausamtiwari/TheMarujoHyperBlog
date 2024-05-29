package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<BlogPost> getAllPosts();

    BlogPost getPostById(Long blogPostId);
    List<BlogPost> getPostsByUser(User user);
    void savePost(BlogPost blogPost);

    void deletePost (Long id);
    Page<BlogPost> findPostPaginated(int pageNo, int pageSize);
}
