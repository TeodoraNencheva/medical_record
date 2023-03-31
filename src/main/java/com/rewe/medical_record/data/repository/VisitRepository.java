package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
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

    @Query("select sum(v.fee.price) from VisitEntity v " +
            "where v.doctor=:doctor")
    BigDecimal getVisitsIncomeByDoctor(DoctorEntity doctor);

    long countAllByPatient(PatientEntity patient);

    @Query("select count(v) from VisitEntity v where :diagnosis member of v.diagnoses")
    long countAllByDiagnosesContaining(DiagnosisEntity diagnosis);

    @Query("select sum(v.fee.price) from VisitEntity v " +
            "where :diagnosis member of v.diagnoses")
    BigDecimal getVisitsIncomeByDiagnosis(DiagnosisEntity diagnosis);

    @Query("select sum(v.fee.price) from VisitEntity v " +
            "where v.paidByPatient=true")
    BigDecimal getVisitsIncomeByNonInsuredPatients();
}
