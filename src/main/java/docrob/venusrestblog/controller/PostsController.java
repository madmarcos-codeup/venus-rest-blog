package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts", headers = "Accept=application/json")
public class PostsController {

    @GetMapping("/")
    public List<Post> fetchPosts() {
        // TODO: go get some posts and return them
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "Post 1", "This is Post 1"));
        posts.add(new Post(2L, "Post 2", "This is Post 2"));

        return posts;
    }

    @GetMapping("/{id}")
    public Post fetchPostById(@PathVariable long id) {
        // TODO: go get some posts and return them
        switch((int) id) {
            case 1:
                return new Post(1L, "Post 1", "This is Post 1");
            case 2:
                return new Post(2L, "Post 2", "This is Post 2");
            default:
                // TODO: respond with a 404
                throw new RuntimeException("Hey man, resource not found");
        }
    }
}
