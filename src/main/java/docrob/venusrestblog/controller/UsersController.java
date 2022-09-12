package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Post;
import docrob.venusrestblog.data.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class UsersController {
    private List<User> users = new ArrayList<>();
    private long nextId = 1;

    @GetMapping("")
    public List<User> fetchUsers() {
        return users;
    }

    @GetMapping("/{id}")
    public User fetchUserById(@PathVariable long id) {
        // search through the list of posts
        // and return the post that matches the given id
        User user = findUserById(id);
        if(user == null) {
            // what to do if we don't find it
            throw new RuntimeException("I don't know what I am doing");
        }

        // we found the post so just return it
        return user;
    }

    @GetMapping("/username/{userName}")
    private User fetchByUserName(@PathVariable String userName) {
        User user = findUserByUserName(userName);
        if(user == null) {
            // what to do if we don't find it
            throw new RuntimeException("I don't know what I am doing");
        }
        return user;
    }

    private User findUserByUserName(String userName) {
        for (User user: users) {
            if(user.getUserName().equals(userName)) {
                return user;
            }
        }
        // didn't find it so do something
        return null;
    }

    private User findUserById(long id) {
        for (User user: users) {
            if(user.getId() == id) {
                return user;
            }
        }
        // didn't find it so do something
        return null;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody User newUser) {
        // assign  nextId to the new post
        newUser.setId(nextId);
        nextId++;

        users.add(newUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        // search through the list of posts
        // and delete the post that matches the given id
        User user = findUserById(id);
        if(user != null) {
            users.remove(user);
            return;
        }
        // what to do if we don't find it
        throw new RuntimeException("User not found");
    }

    @PutMapping("/{id}")
    public void updateUser(@RequestBody User updatedUser, @PathVariable long id) {
        // find the post to update in the posts list

        User user = findUserById(id);
        if(user == null) {
            System.out.println("User not found");
        } else {
            if(updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            return;
        }
        throw new RuntimeException("User not found");
    }
}
