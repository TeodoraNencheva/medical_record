package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralPractitionerRepository extends JpaRepository<GeneralPractitionerEntity, Long> {
}
