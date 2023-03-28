package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
    Optional<VisitEntity> findByIdAndDeletedFalse(Long id);
    List<VisitEntity> findAllByDeletedFalse();
}
