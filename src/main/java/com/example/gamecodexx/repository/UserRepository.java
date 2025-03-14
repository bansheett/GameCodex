package com.example.gamecodexx.repository;

import com.example.gamecodexx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);

}
