package docrob.venusrestblog.controller;

import docrob.venusrestblog.data.Post;
import docrob.venusrestblog.data.User;
import docrob.venusrestblog.data.UserRole;
import docrob.venusrestblog.misc.FieldHelper;
import docrob.venusrestblog.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class UsersController {
    private final PasswordEncoder passwordEncoder;
    private UsersRepository usersRepository;

    @GetMapping("")
    public List<User> fetchUsers() {
        return usersRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> fetchUserById(@PathVariable long id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }
        return optionalUser;
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
        // TODO: validate new user fields

        // don't need the below line at this point but just for kicks
        newUser.setCreatedAt(LocalDate.now());
        newUser.setRole(UserRole.USER);

        String plainTextPassword = newUser.getPassword();
        String encryptedPassword = passwordEncoder.encode(plainTextPassword);
        newUser.setPassword(encryptedPassword);

        usersRepository.save(newUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }
        usersRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@RequestBody User updatedUser, @PathVariable long id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }
        // get the user from the optional so we no longer have to deal with the optional
        User originalUser = optionalUser.get();

        // merge the changed data in updatedUser with originalUser
        BeanUtils.copyProperties(updatedUser, originalUser, FieldHelper.getNullPropertyNames(updatedUser));

        // originalUser now has the merged data (changes + original data)
        originalUser.setId(id);

        usersRepository.save(originalUser);
    }

    @PutMapping("/{id}/updatePassword")
    private void updatePassword(@PathVariable Long id, @RequestParam(required = false) String oldPassword, @RequestParam String newPassword) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found");
        }

        User user = optionalUser.get();

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
