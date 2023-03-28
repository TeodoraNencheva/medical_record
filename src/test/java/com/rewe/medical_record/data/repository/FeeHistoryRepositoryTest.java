package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.FeeHistoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeeHistoryRepositoryTest {
    @Autowired
    private FeeHistoryRepository feeHistoryRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private FeeHistoryEntity first, second, third;

    @BeforeEach
    void setUp() {
        first = new FeeHistoryEntity(LocalDate.of(2023, 2,1), new BigDecimal("3.40"));
        second = new FeeHistoryEntity(LocalDate.of(2023, 3,23), new BigDecimal("3.50"));
        third = new FeeHistoryEntity(LocalDate.of(2022, 3,18), new BigDecimal("3.60"));
    }

    @Test
    @DisplayName("Test get latest fee from non-empty repository")
    void getLatestFeeFromNonEmptyRepository() {
        testEntityManager.persistAndFlush(first);
        testEntityManager.persistAndFlush(second);
        testEntityManager.persistAndFlush(third);
        assertTrue(feeHistoryRepository.findTopByOrderByEffectiveFromDesc().isPresent());
        assertEquals(second, feeHistoryRepository.findTopByOrderByEffectiveFromDesc().get());
    }

    @Test
    @DisplayName("Test get latest fee from empty repository")
    void getLatestFeeFromEmptyRepository() {
        assertTrue(feeHistoryRepository.findTopByOrderByEffectiveFromDesc().isEmpty());
    }
}