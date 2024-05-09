package com.go.training.backend.business;

import com.go.training.backend.entity.User;
import com.go.training.backend.exception.BaseException;
import com.go.training.backend.exception.UserException;
import com.go.training.backend.mapper.UserMapper;
import com.go.training.backend.model.MLoginRequest;
import com.go.training.backend.model.MRegisterRequest;
import com.go.training.backend.model.MRegisterResponse;
import com.go.training.backend.service.TokenService;
import com.go.training.backend.service.UserService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.go.training.backend.exception.FileException;

import org.springframework.web.multipart.MultipartFile;

@Service
public class UserBusiness {
    // Constants for readability
    private static final int MAX_FILE_SIZE_BYTES = 1048576 * 2; // 2 MB
    private static final List<String> SUPPORTED_TYPES = Arrays.asList("image/jpeg", "image/png");

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    public String login(MLoginRequest request) throws BaseException {
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            throw UserException.loginFailedEmailNotFound();
        }

        User user = opt.get();
        boolean matchedPassword = userService.matchPassword(request.getPassword(), user.getPassword());

        if (!matchedPassword) {
            throw UserException.loginFailedIncorrect();
        }

        return tokenService.tokenize(user);
    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        User user = userService.save(request);

        return userMapper.toRegisterResponse(user);
    }

    public String uploadProfilePicture(MultipartFile file) throws FileException {
        if (file == null) {
            throw FileException.fileNull();
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw FileException.unsupported();
        }

        if (!SUPPORTED_TYPES.contains(contentType)) {
            throw FileException.unsupported();
        }

        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
