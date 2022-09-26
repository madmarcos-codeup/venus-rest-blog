package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Category;
import docrob.venusrestblog.data.Post;

import docrob.venusrestblog.data.User;
import docrob.venusrestblog.misc.FieldHelper;
import docrob.venusrestblog.repository.CategoriesRepository;
import docrob.venusrestblog.repository.PostsRepository;
import docrob.venusrestblog.repository.UsersRepository;
import docrob.venusrestblog.service.AuthBuddy;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts", produces = "application/json")
public class PostsController {
    private PostsRepository postsRepository;
    private UsersRepository usersRepository;
    private CategoriesRepository categoriesRepository;

    private AuthBuddy authBuddy;

    @GetMapping("")
    public List<Post> fetchPosts() {
        return postsRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Post> fetchPostById(@PathVariable long id) {
        Optional<Post> optionalPost = postsRepository.findById(id);
        if(optionalPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post id " + id + " not found");
        }
        return optionalPost;
    }

    @PostMapping("")
    public void createPost(@RequestBody Post newPost) {

        // use docrob as author by default
        User author = usersRepository.findById(1L).get();
        newPost.setAuthor(author);
        newPost.setCategories(new ArrayList<>());

        // use first 2 categories for the post by default
        Category cat1 = categoriesRepository.findById(1L).get();
        Category cat2 = categoriesRepository.findById(2L).get();

        newPost.getCategories().add(cat1);
        newPost.getCategories().add(cat2);

        postsRepository.save(newPost);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable long id) {
        Optional<Post> optionalPost = postsRepository.findById(id);
        if(optionalPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post id " + id + " not found");
        }
        postsRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updatePost(@RequestBody Post updatedPost, @PathVariable long id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        User loggedInUser = authBuddy.getUserFromAuthHeader(authHeader);

        Optional<Post> originalPost = postsRepository.findById(id);
        if(originalPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post " + id + " not found");
        }

        // in case id is not in the request body (i.e., updatedPost), set it
        // with the path variable id
        updatedPost.setId(id);

        // copy any new field values FROM updatedPost TO originalPost
        BeanUtils.copyProperties(updatedPost, originalPost.get(), FieldHelper.getNullPropertyNames(updatedPost));

        postsRepository.save(originalPost.get());
    }
}
