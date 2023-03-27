package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;
    @InjectMocks
    private PatientService patientService;
    private PatientEntity firstPatient;
    private PatientEntity secondPatient;

    @BeforeEach
    void setUp() {
        GeneralPractitionerEntity gp = new GeneralPractitionerEntity();
        ReflectionTestUtils.setField(gp, "id", 1L);
        ReflectionTestUtils.setField(gp, "name", "Ivan Ivanov");
        ReflectionTestUtils.setField(gp, "birthDate", LocalDate.of(1980, 2, 2));
        SpecialtyEntity specialty = new SpecialtyEntity();
        ReflectionTestUtils.setField(specialty, "id", 1L);
        ReflectionTestUtils.setField(specialty, "name", "Cardiologist");
        ReflectionTestUtils.setField(gp, "specialties", Set.of(specialty));

        firstPatient = new PatientEntity("Petar Petrov", gp, true);
        ReflectionTestUtils.setField(firstPatient, "id", 1L);
        secondPatient = new PatientEntity("Hristo Hristov", gp, false);
        ReflectionTestUtils.setField(secondPatient, "id", 2L);
    }

    @Test
    @DisplayName("Test get all patients")
    void getAllPatientsTest() {
        List<PatientEntity> list = List.of(firstPatient, secondPatient);
        when(patientRepository.findAll()).thenReturn(list);

        PatientInfoDTO firstDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        PatientInfoDTO secondDto = new PatientInfoDTO(2L, "Hristo Hristov", 1L, false);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(firstDto);
        when(patientMapper.patientEntityToPatientInfoDto(secondPatient)).thenReturn(secondDto);

        assertIterableEquals(List.of(firstDto, secondDto), patientService.getAllPatients());
    }

    @Test
    @DisplayName("Test get patient info test")
    void getPatientInfoTest() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(firstPatient));
        PatientInfoDTO infoDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        when(patientMapper.patientEntityToPatientInfoDto(any(PatientEntity.class))).thenReturn(infoDto);

        assertEquals(infoDto, patientService.getPatientInfo(1L));
    }

    @Test
    @DisplayName("Test add patient")
    void addPatientTest() {
        AddPatientDto patientDto = new AddPatientDto("Petar Petrov", 1L, true);
        when(patientMapper.addPatientDtoToPatientEntity(patientDto)).thenReturn(firstPatient);
        when(patientRepository.save(firstPatient)).thenReturn(firstPatient);

        PatientInfoDTO infoDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(infoDto);

        assertEquals(infoDto, patientService.addPatient(patientDto));
    }
}