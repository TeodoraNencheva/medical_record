package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
}
