package com.go.training.backend;

import com.go.training.backend.entity.Address;
import com.go.training.backend.entity.Social;
import com.go.training.backend.entity.User;
import com.go.training.backend.exception.BaseException;
import com.go.training.backend.exception.UserException;
import com.go.training.backend.model.MRegisterRequest;
import com.go.training.backend.service.AddressService;
import com.go.training.backend.service.SocialService;
import com.go.training.backend.service.UserService;
import java.util.List;
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
    @Autowired
    private UserService userService;

    @Autowired
    private SocialService socialService;

    @Autowired
    private AddressService addressService;

    @Order(1)
    @Test
    void testCreateUser() throws BaseException {
        MRegisterRequest mRegisterRequest = new MRegisterRequest();

        mRegisterRequest.setEmail(TestCreateData.email);
        mRegisterRequest.setPassword(TestCreateData.password);
        mRegisterRequest.setName(TestCreateData.name);

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
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        user.setName(TestUpdateData.name);
        user.setId(user.getId());

        User updateUser = userService.update(user);

        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(TestUpdateData.name, updateUser.getName());
    }

    @Order(3)
    @Test
    void testCreateSocial() throws UserException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        Social social = user.getSocial();
        Assertions.assertNull(social);

        social = socialService.create(user, SocialTestCreateData.facebook, SocialTestCreateData.line, SocialTestCreateData.instagram, SocialTestCreateData.tiktok);

        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestCreateData.facebook, social.getFacebook());
    }

    @Order(3)
    @Test
    void testCreateAddress() {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        List<Address> addresses = user.getAddresses();
        Assertions.assertTrue(addresses.isEmpty());

        createAddress(user, AddressTestCreateData.line1, AddressTestCreateData.line2, AddressTestCreateData.zipcode);
        createAddress(user, AddressTestCreateData2.line1, AddressTestCreateData2.line2, AddressTestCreateData2.zipcode);
    }

    private void createAddress(User user, String line1, String line2, String zipcode) {
        Address address = addressService.create(
                user,
                line1,
                line2,
                zipcode
        );

        Assertions.assertNotNull(address);
        Assertions.assertEquals(line1, address.getLine1());
        Assertions.assertEquals(line2, address.getLine2());
        Assertions.assertEquals(zipcode, address.getZipcode());
    }
    
    @Order(4)
    @Test
    void testDeleteUser() {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        // check social
        Social social = user.getSocial();
        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestCreateData.facebook, social.getFacebook());

        // check address
        List<Address> addresses = user.getAddresses();
        Assertions.assertFalse(addresses.isEmpty());
        Assertions.assertEquals(2, addresses.size());

        userService.deleteById(user.getId());

        Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(optDelete.isEmpty());
    }

    interface TestCreateData {

        String email = "create_test@gmail.com";

        String password = "test123";

        String name = "test_create";

    }

    interface SocialTestCreateData {

        String facebook = "iamnbty";

        String line = "";

        String instagram = "";

        String tiktok = "";

    }

    interface AddressTestCreateData {

        String line1 = "123/4";

        String line2 = "Muang";

        String zipcode = "37000";

    }

    interface AddressTestCreateData2 {

        String line1 = "456/7";

        String line2 = "Muang";

        String zipcode = "37001";

    }

    interface TestUpdateData {

        String name = "test_update";
        String email = "update_test@gmail.com";

    }
}
