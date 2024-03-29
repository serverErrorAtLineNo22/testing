package com.example.springBoot.daos;

import com.example.springBoot.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminDao extends JpaRepository<Admin,Integer> {
    Admin findByName(String name);

    Optional<Admin> findOneByName(String name);
}
