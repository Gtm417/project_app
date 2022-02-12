package org.example.projectapp.auth.controller;

import org.example.projectapp.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//todo created for auth tests purposes
@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {
    private List<User> USERS = List.of(
            User.builder().id(1L).firstName("Ivan").lastName("Ivanov").build(),
            User.builder().id(2L).firstName("Sergey").lastName("Sergeev").build(),
            User.builder().id(3L).firstName("Petr").lastName("Petrov").build()
    );
    

    @GetMapping
    public List<User> getAll() {
        return USERS;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getById(@PathVariable Long id) {
        return USERS.stream().filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User create(@RequestBody User user) {
        this.USERS.add(user);
        return user;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        this.USERS.removeIf(user -> user.getId().equals(id));
    }
}
