package com.example.cs203g1t3.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cs203g1t3.models.User;

import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByUsername(String username);
   Optional<User> findByEmail(String email);
   Optional<User> findByEmailAndPassword(String email, String password);
   Optional<User> findByUsernameAndPassword(String username, String password);
   Optional<User> findByUserID(Long userID);

   Boolean existsByUsername(String username);

   Boolean existsByEmail(String email);

   @Modifying
   @Transactional
   @Query("UPDATE User u SET u.password = :newPassword WHERE u.userID = :userId")
   void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

   @Modifying
   @Transactional
   @Query("UPDATE User u SET u.creditScore = :newCreditScore WHERE u.userID = :userId")
   void updateCreditScore(@Param("userId") Long userId, @Param("newCreditScore") double creditScore);
}
