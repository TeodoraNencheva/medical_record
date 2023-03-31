package com.rewe.medical_record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.medical_record.data.dto.doctor.AddDoctorDto;
import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.dto.doctor.UpdateDoctorDto;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.data.repository.SpecialtyRepository;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.service.DoctorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorRestController.class)
@AutoConfigureMockMvc
class DoctorRestControllerTest {
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private DoctorRepository doctorRepository;
    @MockBean
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all doctors")
    void testGetAllDoctors() throws Exception {
        DoctorInfoDto first = new DoctorInfoDto(1L, "Angel Angelov", LocalDate.of(1982, 6, 6), Set.of("Cardiology", "Pediatrics"));
        DoctorInfoDto second = new DoctorInfoDto(2L, "Stoyan Stoyanov", LocalDate.of(1983, 7, 7), Set.of("Orthopedics"));
        when(doctorService.getAllDoctors()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.size()").value(equalTo(2))))
                .andExpect((jsonPath("$[0].id").value(equalTo(1))))
                .andExpect((jsonPath("$[0].name").value(equalTo("Angel Angelov"))))
                .andExpect((jsonPath("$[0].birthDate").value(equalTo("06.06.1982"))))
                .andExpect((jsonPath("$[0].specialties", containsInAnyOrder("Cardiology", "Pediatrics"))))
                .andExpect((jsonPath("$[1].id").value(equalTo(2))))
                .andExpect((jsonPath("$[1].name").value(equalTo("Stoyan Stoyanov"))))
                .andExpect((jsonPath("$[1].birthDate").value(equalTo("07.07.1983"))))
                .andExpect((jsonPath("$[1].specialties", containsInAnyOrder("Orthopedics"))));
    }

    @Test
    @DisplayName("Test get doctor info with valid id")
    void testGetDoctorInfoWithValidId() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DoctorEntity.class)));
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(1L, "Angel Angelov", LocalDate.of(1982, 6, 6), Set.of("Cardiology", "Pediatrics"));
        when(doctorService.getDoctorInfo(anyLong())).thenReturn(doctorInfoDto);

        mockMvc.perform(get("/doctors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(1))))
                .andExpect((jsonPath("$.name").value(equalTo("Angel Angelov"))))
                .andExpect((jsonPath("$.birthDate").value(equalTo("06.06.1982"))))
                .andExpect((jsonPath("$.specialties", containsInAnyOrder("Cardiology", "Pediatrics"))));
    }

    @Test
    @DisplayName("Test get doctor info with invalid id")
    void testGetDoctorInfoWithInvalidId() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/doctors/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test add doctor")
    void testAddDoctor() throws Exception {
        when(specialtyRepository.findByNameAndDeletedFalse(anyString())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));
        AddDoctorDto addDoctorDto = new AddDoctorDto("Ivan Ivanov", LocalDate.of(1985, 2, 2), Set.of("Anesthesiology", "Dermatology"));
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(5L, "Ivan Ivanov", LocalDate.of(1985, 2, 2), Set.of("Anesthesiology", "Dermatology"));
        when(doctorService.addDoctor(ArgumentMatchers.any(AddDoctorDto.class))).thenReturn(doctorInfoDto);

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDoctorDto)))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id").value(equalTo(5))))
                .andExpect((jsonPath("$.name").value(equalTo("Ivan Ivanov"))))
                .andExpect((jsonPath("$.birthDate").value(equalTo("02.02.1985"))))
                .andExpect((jsonPath("$.specialties", containsInAnyOrder("Anesthesiology", "Dermatology"))));
    }

    @Test
    @DisplayName("Test update doctor with invalid id")
    void testAddDoctorWithInvalidId() throws Exception {
        when(specialtyRepository.findByNameAndDeletedFalse(anyString())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto(1L, "Ivan Ivanov", Set.of("Anesthesiology", "Dermatology"));

        mockMvc.perform(put("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDoctorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test update doctor with valid id")
    void testUpdateDoctorWithValidId() throws Exception {
        when(specialtyRepository.findByNameAndDeletedFalse(anyString())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DoctorEntity.class)));
        UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto(1L, "Ivan Ivanov", Set.of("Anesthesiology", "Dermatology"));
        DoctorInfoDto doctorInfoDto = new DoctorInfoDto(1L, "Angel Angelov", LocalDate.of(1982, 6, 6), Set.of("Cardiology", "Pediatrics"));
        when(doctorService.updateDoctor(ArgumentMatchers.any(UpdateDoctorDto.class))).thenReturn(doctorInfoDto);


        mockMvc.perform(put("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDoctorDto)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(1))))
                .andExpect((jsonPath("$.name").value(equalTo("Angel Angelov"))))
                .andExpect((jsonPath("$.birthDate").value(equalTo("06.06.1982"))))
                .andExpect((jsonPath("$.specialties", containsInAnyOrder("Cardiology", "Pediatrics"))));
    }

    @Test
    @DisplayName("Test delete doctor with valid id")
    void testDeleteDoctorWithValidId() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DoctorEntity.class)));

        mockMvc.perform(delete("/doctors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message").value(equalTo("Doctor with the given ID deleted"))));
    }

    @Test
    @DisplayName("Test delete doctor with invalid id")
    void testDeleteDoctorWithInvalidId() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/doctors/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test getDoctorCountWithGreaterIncomeThan")
    void testGetDoctorCountWithGreaterIncomeThan() throws Exception {
        when(doctorService.countByIncomeGreaterThan(any(BigDecimal.class))).thenReturn(12L);
        mockMvc.perform(get("/doctors/with-greater-income-count")
                        .param("income", "10"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$")).value(equalTo(12)));
    }

    @Test
    @DisplayName("Test get income by valid doctor by non-insured patients")
    void testGetIncomeByValidDoctorByNonInsuredPatients() throws Exception {
        when(doctorService.getIncomeByDoctorByInsuredPatients(anyLong())).thenReturn(new BigDecimal("208.93"));
        mockMvc.perform(get("/doctors/{id}/income-by-insured-patients", 3L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(208.93))));
    }

    @Test
    @DisplayName("Test get income by invalid doctor by non-insured patients")
    void testGetIncomeByInvalidDoctorByNonInsuredPatients() throws Exception {
        when(doctorService.getIncomeByDoctorByInsuredPatients(anyLong())).thenThrow(new DoctorNotFoundException(15L));
        mockMvc.perform(get("/doctors/{id}/income-by-insured-patients", 15L))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$.message").value(equalTo("Doctor with ID 15 not found"))));
    }
}