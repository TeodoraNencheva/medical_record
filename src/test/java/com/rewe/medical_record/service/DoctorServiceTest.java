package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.doctor.AddDoctorDto;
import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.dto.doctor.UpdateDoctorDto;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.mapper.DoctorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorMapper doctorMapper;
    @InjectMocks
    private DoctorService doctorService;
    private DoctorEntity firstDoctor, secondDoctor;

    @BeforeEach
    void setUp() {
        SpecialtyEntity firstSpecialty = new SpecialtyEntity();
        ReflectionTestUtils.setField(firstSpecialty, "id", 1L);
        ReflectionTestUtils.setField(firstSpecialty, "name", "Cardiology");
        SpecialtyEntity secondSpecialty = new SpecialtyEntity();
        ReflectionTestUtils.setField(secondSpecialty, "id", 2L);
        ReflectionTestUtils.setField(secondSpecialty, "name", "Oncology");
        firstDoctor = new DoctorEntity("Ivan Ivanov", LocalDate.of(1985, 1, 1), Set.of(firstSpecialty), false);
        ReflectionTestUtils.setField(firstDoctor, "id", 1L);
        secondDoctor = new DoctorEntity("Hristo Hristov", LocalDate.of(1982, 2, 11), Set.of(firstSpecialty, secondSpecialty), false);
        ReflectionTestUtils.setField(secondDoctor, "id", 2L);
    }

    @Test
    @DisplayName("Test get all doctors with non-empty list")
    void getAllDoctorsNonEmptyListTest() {
        when(doctorRepository.findAllByDeletedFalse()).thenReturn(List.of(firstDoctor, secondDoctor));
        DoctorInfoDto firstDto = new DoctorInfoDto(1L, "Ivan Ivanov", LocalDate.of(1985, 1, 1), Set.of("Cardiologist"));
        DoctorInfoDto secondDto = new DoctorInfoDto(2L, "Hristo Hristov", LocalDate.of(1982, 2, 11), Set.of("Cardiologist"));
        when(doctorMapper.doctorEntityToDoctorInfoDto(firstDoctor)).thenReturn(firstDto);
        when(doctorMapper.doctorEntityToDoctorInfoDto(secondDoctor)).thenReturn(secondDto);

        assertIterableEquals(List.of(firstDto, secondDto), doctorService.getAllDoctors());
    }

    @Test
    @DisplayName("Test get all doctors with empty list")
    void getAllDoctorsEmptyListTest() {
        when(doctorRepository.findAllByDeletedFalse()).thenReturn(List.of());
        assertIterableEquals(List.of(), doctorService.getAllDoctors());
    }

    @Test
    @DisplayName("Test get non-deleted doctor info")
    void getNonDeletedDoctorInfoTest() {
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstDoctor));
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(1L, "Ivan Ivanov", LocalDate.of(1985, 1, 1), Set.of("Cardiologist"));
        when(doctorMapper.doctorEntityToDoctorInfoDto(firstDoctor)).thenReturn(doctorInfoDto);
        assertEquals(doctorInfoDto, doctorService.getDoctorInfo(1L));
    }

    @Test
    @DisplayName("Test get deleted doctor info")
    void getDeletedDoctorInfoTest() {
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorInfo(1L));
    }

    @Test
    @DisplayName("Test add doctor")
    void addDoctorTest() {
        AddDoctorDto addDoctorDto = new AddDoctorDto("Ivan Ivanov", LocalDate.of(1985, 1, 1), Set.of("Cardiologist"));
        when(doctorMapper.addDoctorDtoToDoctorEntity(addDoctorDto)).thenReturn(firstDoctor);
        when(doctorRepository.save(firstDoctor)).thenReturn(firstDoctor);
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(1L, "Ivan Ivanov", LocalDate.of(1985, 1, 1), Set.of("Cardiologist"));
        when(doctorMapper.doctorEntityToDoctorInfoDto(firstDoctor)).thenReturn(doctorInfoDto);

        assertEquals(doctorInfoDto, doctorService.addDoctor(addDoctorDto));
    }

    @Test
    @DisplayName("Test update deleted or non-present doctor")
    void updateDeletedDoctorTest() {
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto(1L, "Ivan Ivanov", Set.of("Cardiologist"));
        assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(updateDoctorDto));
    }

    @Test
    @DisplayName("Test update non-deleted doctor")
    void updateNonDeletedDoctorTest() {
        UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto(1L, "Hristo Hristov", Set.of("Cardiology", "Oncology"));
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstDoctor));
        when(doctorMapper.updateDoctorDtoToDoctorEntity(updateDoctorDto)).thenReturn(secondDoctor);

        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(1L, "Hristo Hristov", LocalDate.of(1985, 1, 1), Set.of("Cardiology", "Oncology"));
        when(doctorRepository.save(firstDoctor)).thenReturn(firstDoctor);
        when(doctorMapper.doctorEntityToDoctorInfoDto(firstDoctor)).thenReturn(doctorInfoDto);

        assertEquals(doctorInfoDto, doctorService.updateDoctor(updateDoctorDto));
        assertEquals(secondDoctor.getName(), firstDoctor.getName());
        assertIterableEquals(secondDoctor.getSpecialties(), firstDoctor.getSpecialties());
    }

    @Test
    @DisplayName("Test deleted deleted or non-present doctor")
    void deleteDeletedDoctorTest() {
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(1L));
    }

    @Test
    @DisplayName("Test deleted non-deleted doctor")
    void deleteNonDeletedDoctor() {
        when(doctorRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstDoctor));
        doctorService.deleteDoctor(1L);
        assertTrue(firstDoctor.isDeleted());
        verify(doctorRepository).save(firstDoctor);
    }

    @Test
    @DisplayName("Test countByIncomeGreaterThan")
    void testCountByIncomeGreaterThan() {
        when(doctorRepository.countByIncomeGreaterThan(any(BigDecimal.class))).thenReturn(5);
        assertEquals(5, doctorService.countByIncomeGreaterThan(new BigDecimal(12)));
    }
}