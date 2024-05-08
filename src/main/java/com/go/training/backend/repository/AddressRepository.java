package com.go.training.backend.repository;

import com.go.training.backend.entity.Address;
import com.go.training.backend.entity.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findByUser(User user);

}