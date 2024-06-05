package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.LikeRepository;
import be.intec.themarujohyperblog.repository.PostRepository;
import be.intec.themarujohyperblog.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private  CommentServiceImpl commentService;




    @Autowired
    public PostServiceImpl(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Autowired
    public void setCommentService(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Override
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

    @Override
    public Page<BlogPost> findUserPostsPaginated(User user, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return this.postRepository.findByUser(user, pageable);
    }

    @Override
    public List<BlogPost> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(BlogPost post) {
        postRepository.save(post);
    }

    public void saveImage(byte[] imageBytes, String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        Files.write(path, imageBytes);
    }

    @Override
    public BlogPost getPost(Long blogPostId) {
        Optional<BlogPost> postOptional = postRepository.findById(blogPostId);
        if (!postOptional.isPresent()) {
            throw new IllegalStateException("Post not found");
        }
        return postOptional.get();
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Page<BlogPost> findPostPaginated(int pageNo, int pageSize) {
        //Default versie, afnemende volgorde
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findPostPaginatedByIDUp(int pageNo, int pageSize) {
        //Sorteert by ID in toenemende volgorde
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findPostPaginatedByIDDown(int pageNo, int pageSize) {
        //Sorteert by ID in afnemende volgorde
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> searchPostDescription(String search) {
        return postRepository.findByDescriptionContaining(search, PageRequest.of(0, 6));

    }




    public int countPosts() {
        return (int) postRepository.count();
    }

    @Override
    @Transactional
    public void likeOrUnlikePost(Long postId, User user) {
        BlogPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
        }
    }
    public int countLikes(BlogPost post) {
        return post.getLikes().size();
    }
}