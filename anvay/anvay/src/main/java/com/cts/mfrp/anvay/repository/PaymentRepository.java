package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByUserId(Integer userId);
    List<Payment> findByInstitutionId(Integer institutionId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'completed'")
    Double sumCompletedPayments();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.institutionId = :institutionId AND p.status = 'completed'")
    Double sumCompletedPaymentsByInstitutionId(Integer institutionId);
}
