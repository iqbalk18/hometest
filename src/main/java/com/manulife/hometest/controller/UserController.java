package com.manulife.hometest.controller;

import com.manulife.hometest.entity.User;
import com.manulife.hometest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
        @Autowired
        private UserService userService;

        @GetMapping
        public List<User> getUsers() {
                return userService.getAllUsers();
        }

        @PostMapping
        public User createUser(@RequestBody User user) {
                return userService.saveUser(user);
        }

        @PutMapping("/{id}")
        public User updateUser(@PathVariable Long id, @RequestBody User user) {
                return userService.updateUser(id, user);
        }

        @DeleteMapping("/{id}")
        public void deleteUser(@PathVariable Long id) {
                userService.deleteUser(id);
        }
}


