package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VisitRepositoryTest {
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private VisitEntity first, second, third;

    @BeforeEach
    void setUp() {
        PatientEntity patient1 = new PatientEntity("Ivan Ivanov", null, false, false);
        PatientEntity patient2 = new PatientEntity("Angel Angelov", null, true, false);
        DoctorEntity doctor1 = new DoctorEntity("Stoyan Stoyanov", LocalDate.of(1980, 4, 4), Set.of(), false);
        DoctorEntity doctor2 = new DoctorEntity("Nikola Nikolov", LocalDate.of(1970, 5, 5), Set.of(), false);
        DiagnosisEntity diagnosis1 = new DiagnosisEntity("healthy", "healthy", false);
        DiagnosisEntity diagnosis2 = new DiagnosisEntity("flu", "flu", false);
        FeeHistoryEntity fee = new FeeHistoryEntity(LocalDate.of(2022, 8, 8), new BigDecimal("3.30"));
        testEntityManager.persistAndFlush(patient1);
        testEntityManager.persistAndFlush(patient2);
        testEntityManager.persistAndFlush(doctor1);
        testEntityManager.persistAndFlush(doctor2);
        testEntityManager.persistAndFlush(diagnosis1);
        testEntityManager.persistAndFlush(diagnosis2);
        testEntityManager.persistAndFlush(fee);

        first = new VisitEntity(patient1, doctor1, LocalDateTime.now(), Set.of(diagnosis1), fee, true, false);
        second = new VisitEntity(patient1, doctor2, LocalDateTime.now(), Set.of(diagnosis2), fee, true, false);
        third = new VisitEntity(patient2, doctor2, LocalDateTime.now(), Set.of(diagnosis2), fee, false, false);
    }

    @Test
    @DisplayName("Test get all visits income")
    void testGetAllVisitsIncome() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);

        assertEquals(new BigDecimal("9.90"), visitRepository.getAllVisitsIncome());
    }

    @Test
    @DisplayName("Test get all visits income by doctor id")
    void testGetAllVisitsIncomeByDoctorId() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);

        assertEquals(new BigDecimal("3.30"), visitRepository.getVisitsIncomeByDoctor(first.getDoctor()));
        assertEquals(new BigDecimal("6.60"), visitRepository.getVisitsIncomeByDoctor(second.getDoctor()));
    }

    @Test
    @DisplayName("Test count all by patient id")
    void countAllByPatientIdTest() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);

        assertEquals(2L, visitRepository.countAllByPatient(first.getPatient()));
        assertEquals(1L, visitRepository.countAllByPatient(third.getPatient()));
    }

    @Test
    @DisplayName("Test count all by diagnosis id")
    void countAllByDiagnosisIdTest() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);

        assertEquals(1L, visitRepository.countAllByDiagnosesContaining(first.getDiagnoses().iterator().next()));
        assertEquals(2L, visitRepository.countAllByDiagnosesContaining(second.getDiagnoses().iterator().next()));
    }

    @Test
    @DisplayName("Test get visits income by diagnosis")
    void testGetVisitsIncomeByDiagnosis() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);

        assertEquals(new BigDecimal("3.30"), visitRepository.getVisitsIncomeByDiagnosis(first.getDiagnoses().iterator().next()));
        assertEquals(new BigDecimal("6.60"), visitRepository.getVisitsIncomeByDiagnosis(second.getDiagnoses().iterator().next()));
    }

    @Test
    @DisplayName("Test get visits income by non-insured patients")
    void testGetVisitsIncomeByNonInsuredPatients() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);
        assertEquals(new BigDecimal("6.60"), visitRepository.getVisitsIncomeByNonInsuredPatients());
    }
}