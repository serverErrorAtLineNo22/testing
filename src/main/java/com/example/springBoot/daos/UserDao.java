package com.example.springBoot.daos;

import com.example.springBoot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer> {
    Optional<User> findOneByName(String name);
    User findByName(String userName);
    User findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String searchQuery);

    List<User> findByEmailContainingIgnoreCase(String searchQuery);

    @Query("UPDATE User u SET u.enabled = true WHERE u.id = ?1")
    @Modifying
    public void enable(Integer id);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    List<User> findAllByStatus(User.Status status);
}
