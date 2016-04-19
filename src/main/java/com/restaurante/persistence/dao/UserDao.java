package com.restaurante.persistence.dao;

import com.restaurante.persistence.Table;
import com.restaurante.persistence.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
