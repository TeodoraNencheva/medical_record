package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private PatientEntity firstPatient, secondPatient, thirdPatient;
    @BeforeEach
    void setUp() {
        GeneralPractitionerEntity gp = new GeneralPractitionerEntity();
        ReflectionTestUtils.setField(gp, "name", "Nikola Nikolov");
        ReflectionTestUtils.setField(gp, "birthDate", LocalDate.of(1980, 1, 1));
        ReflectionTestUtils.setField(gp, "specialties", Set.of());
        testEntityManager.persistAndFlush(gp);

        firstPatient = new PatientEntity("Ivan Ivanov", gp, false, false);
        secondPatient = new PatientEntity("Hristo Hristov", gp, false, true);
        thirdPatient = new PatientEntity("Stoyan Stoyanov", gp, false, false);
    }
    @Test
    @DisplayName("Test find all non-deleted patients returns non-empty list")
    void findAllNonDeletedReturnsNonEmptyListTest() {
        testEntityManager.persistAndFlush(firstPatient);
        testEntityManager.persistAndFlush(secondPatient);
        testEntityManager.persistAndFlush(thirdPatient);
        assertIterableEquals(List.of(firstPatient, thirdPatient), patientRepository.findAllByDeletedFalse());
    }

    @Test
    @DisplayName("Test find all non-deleted patients returns empty list")
    void findAllNonDeletedReturnsEmptyListTest() {
        ReflectionTestUtils.setField(firstPatient, "deleted", true);
        ReflectionTestUtils.setField(thirdPatient, "deleted", true);
        testEntityManager.persistAndFlush(firstPatient);
        testEntityManager.persistAndFlush(secondPatient);
        testEntityManager.persistAndFlush(thirdPatient);

        assertIterableEquals(List.of(), patientRepository.findAllByDeletedFalse());
    }

    @Test
    @DisplayName("Test find non-deleted patient returns non-empty Optional")
    void findByIdNonDeletedReturnNonEmptyOptional() {
        PatientEntity expected = testEntityManager.persistAndFlush(firstPatient);
        Optional<PatientEntity> actual = patientRepository.findByIdAndDeletedFalse(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(firstPatient, actual.get());
    }

    @Test
    @DisplayName("Test find non-deleted patient returns empty Optional")
    void findByIdNonDeletedReturnEmptyOptional() {
        PatientEntity result = testEntityManager.persistAndFlush(secondPatient);
        assertTrue(patientRepository.findByIdAndDeletedFalse(result.getId()).isEmpty());
    }
}