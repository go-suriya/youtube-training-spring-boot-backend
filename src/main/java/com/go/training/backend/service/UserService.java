package com.go.training.backend.service;

import com.go.training.backend.entity.User;
import com.go.training.backend.exception.BaseException;
import com.go.training.backend.exception.UserException;
import com.go.training.backend.model.MRegisterRequest;
import com.go.training.backend.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
      return userRepository.findByEmail(email);
    }

    public boolean matchPassword(String rawPassword, String encodePassword) {
        return passwordEncoder.matches(rawPassword, encodePassword);
    }

    public void deleteById(String id) {
         userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User save(MRegisterRequest user) throws BaseException {
        // validate
        if (Objects.isNull(user.getEmail())) {
            throw UserException.createEmailNull();
        }

        if (Objects.isNull(user.getPassword())) {
            throw UserException.createPasswordNull();
        }

        if (Objects.isNull(user.getName())) {
            throw UserException.createNameNull();
        }

        // verify
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if (existsByEmail) {
            throw UserException.createEmailDuplicate();
        }

        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setName(user.getName());

        return userRepository.save(userEntity);
    }

}
