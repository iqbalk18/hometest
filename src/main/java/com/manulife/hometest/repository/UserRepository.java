package com.manulife.hometest.repository;

import com.manulife.hometest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
}