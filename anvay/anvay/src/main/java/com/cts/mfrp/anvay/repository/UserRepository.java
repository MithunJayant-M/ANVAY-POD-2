package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByInstitutionId(Integer institutionId);
    long countByInstitutionId(Integer institutionId);
    long countByRole(String role);
}
