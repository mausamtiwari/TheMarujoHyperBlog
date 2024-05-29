package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import be.intec.themarujohyperblog.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {




    BlogPost getPostById(Long blogPostId);




    List<BlogPost> getPostsByUser(User user); // Add this method

    Page<BlogPost> findUserPostsPaginated(User user, int pageNo, int pageSize);



    List<BlogPost> getAllPosts();
    void savePost(BlogPost post);

    BlogPost getPost(Long id);

    void deletePost(Long id);

    Page<BlogPost> findPostPaginated(int pageNo, int pageSize);
    Page<BlogPost> findPostPaginatedByIDUp(int pageNo, int pageSize);
    Page<BlogPost> findPostPaginatedByIDDown(int pageNo, int pageSize);

    Page<BlogPost> searchPostDescription(String search);
}

