package com.cts.mfrp.anvay.repository;

import com.cts.mfrp.anvay.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {
    Optional<Institution> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByStatus(String status);
    List<Institution> findByStatus(String status);

    @Query("SELECT i FROM Institution i WHERE " +
           "(:search IS NULL OR LOWER(i.institutionName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(i.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Institution> searchInstitutions(@Param("search") String search);
}
