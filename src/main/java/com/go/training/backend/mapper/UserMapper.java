package com.go.training.backend.mapper;

import com.go.training.backend.entity.User;
import com.go.training.backend.model.MRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    MRegisterResponse toRegisterResponse(User user);
}
