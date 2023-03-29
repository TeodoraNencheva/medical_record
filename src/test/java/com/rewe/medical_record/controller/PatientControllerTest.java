package com.rewe.medical_record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc
class PatientControllerTest {
    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private GeneralPractitionerRepository gpRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all patients")
    void getAllPatientsTest() throws Exception {
        PatientInfoDTO first = new PatientInfoDTO(1L, "Ivan Ivanov", 2L, false);
        PatientInfoDTO second = new PatientInfoDTO(2L, "Hristo Hristov", 1L, true);
        when(patientService.getAllPatients()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.size()").value(equalTo(2))))
                .andExpect((jsonPath("$[0].id").value(equalTo(1))))
                .andExpect((jsonPath("$[0].name").value(equalTo("Ivan Ivanov"))))
                .andExpect((jsonPath("$[0].gpId").value(equalTo(2))))
                .andExpect((jsonPath("$[0].insured").value(equalTo(false))))
                .andExpect((jsonPath("$[1].id").value(equalTo(2))))
                .andExpect((jsonPath("$[1].name").value(equalTo("Hristo Hristov"))))
                .andExpect((jsonPath("$[1].gpId").value(equalTo(1))))
                .andExpect((jsonPath("$[1].insured").value(equalTo(true))))
                .andDo(print());
    }

    @Test
    @DisplayName("Test get patient info with valid id")
    void getPatientInfoWithValidIdTest() throws Exception {
        PatientInfoDTO dto = new PatientInfoDTO(2L, "Hristo Hristov", 1L, true);
        when(patientService.getPatientInfo(anyLong())).thenReturn(dto);
        when(patientRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(PatientEntity.class)));

        mockMvc.perform(get("/patient/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(2))))
                .andExpect((jsonPath("$.name").value(equalTo("Hristo Hristov"))))
                .andExpect((jsonPath("$.gpId").value(equalTo(1))))
                .andExpect((jsonPath("$.insured").value(equalTo(true))))
                .andDo(print());
    }

    @Test
    @DisplayName("Test get patient info with invalid id")
    void getPatientInfoWithInvalidIdTest() throws Exception {
        when(patientRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/patient/{id}", 2L))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Test add patient")
    void addPatientTest() throws Exception {
        AddPatientDto addDto = new AddPatientDto("Ivan Ivanov", 1L, true);
        PatientInfoDTO infoDTO = new PatientInfoDTO(1L, "Ivan Ivanov", 1L, true);
        when(patientService.addPatient(any(AddPatientDto.class))).thenReturn(infoDTO);
        when(gpRepository.findById(anyLong())).thenReturn(Optional.of(mock(GeneralPractitionerEntity.class)));

        mockMvc.perform(post("/patient")
                        .content(objectMapper.writeValueAsString(addDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id").value(equalTo(1))))
                .andExpect((jsonPath("$.name").value(equalTo("Ivan Ivanov"))))
                .andExpect((jsonPath("$.gpId").value(equalTo(1))))
                .andExpect((jsonPath("$.insured").value(equalTo(true))))
                .andDo(print());
    }
}