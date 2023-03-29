package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
    Optional<VisitEntity> findByIdAndDeletedFalse(Long id);
    List<VisitEntity> findAllByDeletedFalse();
    @Query("SELECT SUM(v.fee.price) FROM VisitEntity v")
    BigDecimal getAllVisitsIncome();
    @Query("SELECT SUM(v.fee.price) FROM VisitEntity v where v.doctor.id = ?1")
    BigDecimal getVisitsIncomeByDoctorId(Long doctorId);
    @Query("SELECT count(v.id) FROM VisitEntity v where v.patient.id = ?1")
    int getVisitsCountByPatientId(Long patientId);
    int countAllByDiagnosesContaining(DiagnosisEntity diagnosisEntity);
}
