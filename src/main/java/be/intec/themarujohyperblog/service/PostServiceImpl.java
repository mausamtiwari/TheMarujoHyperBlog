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

import java.util.*;


@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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
    public List<BlogPost> searchPostsByTitleDescriptionContentContaining(String search) {

        List<BlogPost> searchDescriptionResult = postRepository.findByDescriptionContaining(search);
        List<BlogPost> searchContentResult = postRepository.findByContentContaining(search);
        List<BlogPost> searchTitleResult = postRepository.findByTitleContaining(search);

        //Resultaten in een Set samenvoegen (=unieke waarden) en terug omzetten naar een gewone List:

        Set<BlogPost> searchResultSet = new HashSet<>();
        searchResultSet.addAll(searchDescriptionResult);
        searchResultSet.addAll(searchContentResult);
        searchResultSet.addAll(searchTitleResult);

        List<BlogPost> searchResultList = new ArrayList<>(searchResultSet);

        return searchResultList;
    }

    @Override
    public void savePost(BlogPost post) {
        postRepository.save(post);
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
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findPostPaginatedByIDUp(int pageNo, int pageSize) {
        //Sorteert by ID in toenemende volgorde
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findPostPaginatedByIDDown(int pageNo, int pageSize) {
        //Sorteert by ID in afnemende volgorde
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }



    public long countAllBlogPosts() {
        return (int) postRepository.count();
    }

    //missing?
    //end missing methods?
/*
    @Override
    public List<BlogPost> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(BlogPost post) {
        postRepository.save(post);
    }


    @Override
    public List<BlogPost> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    public BlogPost getPostById(Long blogPostId) {
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
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }


    @Override
    public Page<BlogPost> findPostPaginatedByIDUp(int pageNo, int pageSize) {
        //Sorteert by ID in toenemende volgorde
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findPostPaginatedByIDDown(int pageNo, int pageSize) {
        //Sorteert by ID in afnemende volgorde
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo-1 , pageSize, sort); //waarom pageNumber-1?
        //Pageable is een interface, het object bevat instructies voor welke paginas, hoeveel informatie, en de sortering is ook mogelijk.
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<BlogPost> findUserPostsPaginated(User user, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return this.postRepository.findByUser(user, pageable);
    }
*/

}