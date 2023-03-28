package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    List<DoctorEntity> findAllByDeletedFalse();
    Optional<DoctorEntity> findByIdAndDeletedFalse(Long id);
}
