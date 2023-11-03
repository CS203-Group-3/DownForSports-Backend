package com.example.cs203g1t3.repository;

import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.stereotype.Repository;

import com.example.cs203g1t3.models.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
    
}
