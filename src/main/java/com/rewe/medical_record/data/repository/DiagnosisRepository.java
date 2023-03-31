package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DiagnosisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<DiagnosisEntity, Long> {
    Optional<DiagnosisEntity> findByIdAndDeletedFalse(Long id);
    List<DiagnosisEntity> findAllByDeletedFalse();
    Optional<DiagnosisEntity> findByNameAndDeletedFalse(String name);
}