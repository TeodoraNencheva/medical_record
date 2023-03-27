package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    List<PatientEntity> findAllByDeletedFalse();
    Optional<PatientEntity> findByIdAndDeletedFalse(Long id);
}
