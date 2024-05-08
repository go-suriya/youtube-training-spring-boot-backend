package com.go.training.backend.repository;

import com.go.training.backend.entity.Social;
import com.go.training.backend.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends CrudRepository<Social, String> {
    Optional<Social> findByUser(User user);
}