package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Post;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts", produces = "application/json")
public class PostsController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping("/")
//    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Post> fetchPosts() {
        return posts;
    }

    @GetMapping("/{id}")
    public Post fetchPostById(@PathVariable long id) {
        // search through the list of posts
        // and return the post that matches the given id
        for (Post post: posts) {
            if(post.getId() == id) {
                return post;
            }
        }
        // what to do if we don't find it
        throw new RuntimeException("I don't know what I am doing");
    }

    @PostMapping("/")
    public void createPost(@RequestBody Post newPost) {
//        System.out.println(newPost);
        posts.add(newPost);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable long id) {
        // search through the list of posts
        // and delete the post that matches the given id
        for (Post post: posts) {
            if(post.getId() == id) {
                // if we find the post then delete it
                posts.remove(post);
                return;
            }
        }
        // what to do if we don't find it
        throw new RuntimeException("I don't know what I am doing");
    }

}
