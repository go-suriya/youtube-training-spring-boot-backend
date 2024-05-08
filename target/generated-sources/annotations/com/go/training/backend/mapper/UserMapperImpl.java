package com.go.training.backend.mapper;

import com.go.training.backend.entity.User;
import com.go.training.backend.model.MRegisterResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-08T15:11:23+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public MRegisterResponse toRegisterResponse(User user) {
        if ( user == null ) {
            return null;
        }

        MRegisterResponse.MRegisterResponseBuilder mRegisterResponse = MRegisterResponse.builder();

        mRegisterResponse.email( user.getEmail() );
        mRegisterResponse.name( user.getName() );

        return mRegisterResponse.build();
    }
}
