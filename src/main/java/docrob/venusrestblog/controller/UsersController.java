package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.User;
import docrob.venusrestblog.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class UsersController {
    private UsersRepository usersRepository;

    @GetMapping("")
    public List<User> fetchUsers() {
        return usersRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> fetchUserById(@PathVariable long id) {
        Optional<User> user = usersRepository.findById(id);
        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }
        return user;
    }

    @GetMapping("/me")
    private Optional<User> fetchMe() {
        return usersRepository.findById(1L);
    }

//    @GetMapping("/username/{userName}")
//    private User fetchByUserName(@PathVariable String userName) {
//
//    }

//    @GetMapping("/email/{email}")
//    private User fetchByEmail(@PathVariable String email) {
//        User user = findUserByEmail(email);
//        if(user == null) {
//            // what to do if we don't find it
//            throw new RuntimeException("I don't know what I am doing");
//        }
//        return user;
//    }

    @PostMapping("/create")
    public void createUser(@RequestBody User newUser) {
        // don't need the below line at this point but just for kicks
        newUser.setCreatedAt(LocalDate.now());
        usersRepository.save(newUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        usersRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@RequestBody User updatedUser, @PathVariable long id) {
        // find the post to update in the posts list
        updatedUser.setId(id);
        usersRepository.save(updatedUser);
    }

    @PutMapping("/{id}/updatePassword")
    private void updatePassword(@PathVariable Long id, @RequestParam(required = false) String oldPassword, @RequestParam String newPassword) {
        User user = usersRepository.findById(id).get();
//        if(user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " not found");
//        }

        // compare old password with saved pw
        if(!user.getPassword().equals(oldPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "amscray");
        }

        // validate new password
        if(newPassword.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new pw length must be at least 3 characters");
        }

        user.setPassword(newPassword);
        usersRepository.save(user);
    }
}
