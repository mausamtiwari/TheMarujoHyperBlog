package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    public List<BlogPost> getAllPosts() {
        return postRepository.findAll();
    }

    public BlogPost getPostById(Long blogPostId) {
        Optional<BlogPost> postOptional = postRepository.findById(blogPostId);
        if (!postOptional.isPresent()) {
            throw new IllegalStateException("Post not found");
        }
        return postOptional.get();
    }
    @Override
    public List<BlogPost> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }
    public void savePost(BlogPost blogPost) {
        postRepository.save(blogPost);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<BlogPost> findPostPaginated(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

}
