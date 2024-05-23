package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<BlogPost> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(BlogPost post) {
        postRepository.save(post);
    }

    @Override
    public BlogPost getPost(Long id) {
       Optional<BlogPost> postOptional = postRepository.findById(id);
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }
}
