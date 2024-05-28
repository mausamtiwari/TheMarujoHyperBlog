package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<BlogPost> getAllPosts();

    BlogPost getPostById(Long blogPostId);

    void savePost(BlogPost blogPost);

    void deletePost (Long id);
    Page<BlogPost> findPostPaginated(int pageNo, int pageSize);
}
