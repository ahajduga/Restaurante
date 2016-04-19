package com.restaurante.persistence.dao;

import com.restaurante.persistence.UserRole;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alex on 07.05.15.
 */
public interface UserRoleDao extends CrudRepository<UserRole,Long> {
}
