package com.example.cs203g1t3.repository;

import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cs203g1t3.models.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
    
    @Modifying
    @Query("DELETE FROM CreditRequest cr WHERE cr.creditID = :creditRequestId")
    void deleteByCreditRequestId(@Param("creditRequestId") Long creditRequestId);
}
