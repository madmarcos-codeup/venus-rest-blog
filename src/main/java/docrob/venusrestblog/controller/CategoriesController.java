package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Category;
import docrob.venusrestblog.data.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/api/categories", produces = "application/json")
public class CategoriesController {

    @GetMapping("")
    private Category fetchPostsByCategory(@RequestParam String categoryName) {
        Category category = new Category(1L, categoryName, null);

        ArrayList<Post> fakePosts = new ArrayList<>();
        fakePosts.add(new Post(1L, "Bunnies Rock!", "for real!", null, new ArrayList<>(Arrays.asList(new Category(1L, categoryName, null)))));
        fakePosts.add(new Post(2L, "Get a life", "for really real", null, new ArrayList<>(Arrays.asList(new Category(1L, categoryName, null)))));
        category.setPosts(fakePosts);

        return category;
    }

}
