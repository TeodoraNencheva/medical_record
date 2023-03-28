package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DoctorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}