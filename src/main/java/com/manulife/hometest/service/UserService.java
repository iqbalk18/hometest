package com.manulife.hometest.service;

import com.manulife.hometest.entity.User;
import com.manulife.hometest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
        @Autowired
        private UserRepository userRepository;

        public List<User> getAllUsers() {
                return userRepository.findAll(); }
        public User saveUser(User user) {
                return userRepository.save(user); }
        public void deleteUser(Long id) {
                userRepository.deleteById(id); }

        public Optional<User> getUserById(Long id) {
                return userRepository.findById(id); }

        public List<User> getUsersByIds(List<Long> ids) {
                return userRepository.findAllById(ids);
        }
        public User updateUser(Long id, User userDetails) {
                return userRepository.findById(id).map(user -> {
                        user.setFirstName(userDetails.getFirstName());
                        user.setLastName(userDetails.getLastName());
                        user.setEmail(userDetails.getEmail());
                        user.setDateOfBirth(userDetails.getDateOfBirth());
                        user.setGender(userDetails.getGender());
                        return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
        }
}


