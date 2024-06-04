package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {


    BlogPost getPostById(Long blogPostId);


    List<BlogPost> getPostsByUser(User user); // Add this method

    Page<BlogPost> findUserPostsPaginated(User user, int pageNo, int pageSize);


    List<BlogPost> getAllPosts();

    List<BlogPost> searchPostsByTitleDescriptionContentContaining(String description);

    void savePost(BlogPost post);

    BlogPost getPost(Long id);

    void deletePost(Long id);

    Page<BlogPost> findPostPaginated(int pageNo, int pageSize);

    Page<BlogPost> findPostPaginatedByIDUp(int pageNo, int pageSize);

    Page<BlogPost> findPostPaginatedByIDDown(int pageNo, int pageSize);




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



