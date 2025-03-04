package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  public Optional<User> findByUsername(String username);
}
