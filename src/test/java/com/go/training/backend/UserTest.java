package com.go.training.backend;

import com.go.training.backend.entity.User;
import com.go.training.backend.exception.BaseException;
import com.go.training.backend.model.MRegisterRequest;
import com.go.training.backend.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    String EMAIL_CREATED = "create_test@gmail.com";
    String EMAIL_UPDATED = "update_test@gmail.com";
    String PASSWORD = "test123";
    String NAME = "test";

    @Autowired
    private UserService userService;

    @Order(1)
    @Test
    void testCreateUser() throws BaseException {
        MRegisterRequest mRegisterRequest = new MRegisterRequest();

        mRegisterRequest.setEmail(EMAIL_CREATED);
        mRegisterRequest.setPassword(PASSWORD);
        mRegisterRequest.setName(NAME);

        User user = userService.save(mRegisterRequest);

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());

        Assertions.assertEquals(mRegisterRequest.getEmail(), user.getEmail());

        boolean isMatched = userService.matchPassword(mRegisterRequest.getPassword(), user.getPassword());
        Assertions.assertTrue(isMatched);

        Assertions.assertEquals(mRegisterRequest.getName(), user.getName());
    }

    @Order(2)
    @Test
    void testUpdateUser() {
        Optional<User> opt = userService.findByEmail(EMAIL_CREATED);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        user.setName(NAME);
        user.setEmail(EMAIL_UPDATED);
        user.setPassword(PASSWORD);
        user.setId(user.getId());

        User updateUser = userService.update(user);

        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(EMAIL_UPDATED, user.getEmail());
    }

    @Order(3)
    @Test
    void testDeleteUser() {
        Optional<User> opt = userService.findByEmail(EMAIL_UPDATED);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        userService.deleteById(user.getId());

        Optional<User> optCheck = userService.findByEmail(EMAIL_UPDATED);
        Assertions.assertTrue(optCheck.isEmpty());
    }
}
