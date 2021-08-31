package com.sda.auction.repository;

import com.sda.auction.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Optional container for Role, method findByName finds the role by name
    Optional<Role> findByName(String name);

}
