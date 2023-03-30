package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DoctorRepositoryTest {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private DoctorEntity firstDoctor, secondDoctor, thirdDoctor;

    @BeforeEach
    void setUp() {
        firstDoctor = new DoctorEntity("Ivan Ivanov", LocalDate.of(1980, 1, 1), Set.of(), false);
        secondDoctor = new DoctorEntity("Hristo Hristov", LocalDate.of(1981, 1, 1), Set.of(), true);
        thirdDoctor = new DoctorEntity("Stoyan Stoyanov", LocalDate.of(1982, 1, 1), Set.of(), false);
    }

    @Test
    @DisplayName("Test find all non-deleted doctor return non-empty list")
    void findAllNonDeletedDoctorsReturnsNonEmptyList() {
        testEntityManager.persistAndFlush(firstDoctor);
        testEntityManager.persistAndFlush(secondDoctor);
        testEntityManager.persistAndFlush(thirdDoctor);
        assertIterableEquals(List.of(firstDoctor, thirdDoctor), doctorRepository.findAllByDeletedFalse());
    }

    @Test
    @DisplayName("Test find all non-deleted doctors return empty list")
    void findAllNonDeletedDoctorsReturnsEmptyList() {
        ReflectionTestUtils.setField(firstDoctor, "deleted", true);
        ReflectionTestUtils.setField(thirdDoctor, "deleted", true);
        testEntityManager.persistAndFlush(firstDoctor);
        testEntityManager.persistAndFlush(secondDoctor);
        testEntityManager.persistAndFlush(thirdDoctor);

        assertIterableEquals(List.of(), doctorRepository.findAllByDeletedFalse());
    }

    @Test
    @DisplayName("Test find non-deleted doctor by id returns non-empty Optional")
    void findNonDeletedDoctorByIdReturnsNonEmptyOptional() {
        DoctorEntity expected = testEntityManager.persistAndFlush(firstDoctor);
        Optional<DoctorEntity> actual = doctorRepository.findByIdAndDeletedFalse(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(firstDoctor, actual.get());
    }

    @Test
    @DisplayName("Test find deleted doctor by id returns empty Optional")
    void findDeletedDoctorByIdReturnsEmptyOptional() {
        DoctorEntity expected = testEntityManager.persistAndFlush(secondDoctor);
        assertTrue(doctorRepository.findByIdAndDeletedFalse(expected.getId()).isEmpty());
    }

    @Test
    @DisplayName("Test countByIncomeGreaterThan with empty repository")
    void countByIncomeGreaterThanTestWithEmptyRepository() {
        assertEquals(0, doctorRepository.countByIncomeGreaterThan(new BigDecimal(10)));
    }

    @Test
    @DisplayName("Test countByIncomeGreaterThan returns result")
    void countByIncomeGreaterThanReturnsResult() {
        testEntityManager.persistAndFlush(firstDoctor);
        testEntityManager.persistAndFlush(secondDoctor);
        testEntityManager.persistAndFlush(thirdDoctor);

        FeeHistoryEntity fee = new FeeHistoryEntity(LocalDate.of(2022, 10, 20), new BigDecimal("2.50"));
        testEntityManager.persistAndFlush(fee);
        GeneralPractitionerEntity gp = new GeneralPractitionerEntity();
        ReflectionTestUtils.setField(gp, "name", "Petar Petrov");
        ReflectionTestUtils.setField(gp, "birthDate", LocalDate.of(1990, 10, 10));
        testEntityManager.persistAndFlush(gp);
        PatientEntity patient = new PatientEntity("Ivan Ivanov", gp, true, false);
        testEntityManager.persistAndFlush(patient);

        VisitEntity firstDoctorVisit1 = new VisitEntity(patient, firstDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(firstDoctorVisit1);
        VisitEntity firstDoctorVisit2 = new VisitEntity(patient, firstDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(firstDoctorVisit2);
        VisitEntity firstDoctorVisit3 = new VisitEntity(patient, firstDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(firstDoctorVisit3);

        VisitEntity secondDoctorVisit = new VisitEntity(patient, secondDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(secondDoctorVisit);

        VisitEntity thirdDoctorVisit1 = new VisitEntity(patient, thirdDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(thirdDoctorVisit1);
        VisitEntity thirdDoctorVisit2 = new VisitEntity(patient, thirdDoctor, LocalDateTime.now(), Set.of(), fee, false, false);
        testEntityManager.persistAndFlush(thirdDoctorVisit2);

        assertEquals(0, doctorRepository.countByIncomeGreaterThan(new BigDecimal(8)));
        assertEquals(1, doctorRepository.countByIncomeGreaterThan(new BigDecimal(5)));
        assertEquals(2, doctorRepository.countByIncomeGreaterThan(new BigDecimal(4)));
        assertEquals(3, doctorRepository.countByIncomeGreaterThan(new BigDecimal(2)));
    }
}