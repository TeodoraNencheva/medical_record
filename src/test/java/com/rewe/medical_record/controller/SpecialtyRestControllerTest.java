package com.rewe.medical_record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.medical_record.data.dto.specialty.AddSpecialtyDto;
import com.rewe.medical_record.data.dto.specialty.SpecialtyInfoDto;
import com.rewe.medical_record.data.dto.specialty.UpdateSpecialtyDto;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.SpecialtyRepository;
import com.rewe.medical_record.service.SpecialtyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpecialtyRestController.class)
@AutoConfigureMockMvc
class SpecialtyRestControllerTest {
    @MockBean
    private SpecialtyService specialtyService;
    @MockBean
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all specialties")
    void testGetAllSpecialties() throws Exception {
        SpecialtyInfoDto infoDto1 = new SpecialtyInfoDto(1L, "Cardiology", "Cardiology");
        SpecialtyInfoDto infoDto2 = new SpecialtyInfoDto(2L, "Dermatology", "Dermatology");
        when(specialtyService.getAllSpecialties()).thenReturn(List.of(infoDto1, infoDto2));

        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.size()").value(equalTo(2))))
                .andExpect((jsonPath("$[0].id").value(equalTo(1))))
                .andExpect((jsonPath("$[0].name").value(equalTo("Cardiology"))))
                .andExpect((jsonPath("$[0].description").value(equalTo("Cardiology"))))
                .andExpect((jsonPath("$[1].id").value(equalTo(2))))
                .andExpect((jsonPath("$[1].name").value(equalTo("Dermatology"))))
                .andExpect((jsonPath("$[1].description").value(equalTo("Dermatology"))));
    }

    @Test
    @DisplayName("Test get specialty with valid id")
    void testGetSpecialty() throws Exception {
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(2L, "Dermatology", "Dermatology");
        when(specialtyService.getSpecialty(anyLong())).thenReturn(infoDto);
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));

        mockMvc.perform(get("/specialties/{id}", 1))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(2))))
                .andExpect((jsonPath("$.name").value(equalTo("Dermatology"))))
                .andExpect((jsonPath("$.description").value(equalTo("Dermatology"))));
    }

    @Test
    @DisplayName("Test get specialty with invalid id")
    void testGetSpecialtyWithInvalidId() throws Exception {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/specialties/{id}", 1))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No specialty with the given ID found"))));
    }

    @Test
    @DisplayName("Test create specialty")
    void testCreateSpecialty() throws Exception {
        AddSpecialtyDto addSpecialtyDto = new AddSpecialtyDto("Cardiology", "Cardiology");
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(1L, "Cardiology", "Cardiology");
        when(specialtyService.createSpecialty(any(AddSpecialtyDto.class))).thenReturn(infoDto);
        mockMvc.perform(post("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addSpecialtyDto)))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id").value(equalTo(1))))
                .andExpect((jsonPath("$.name").value(equalTo("Cardiology"))))
                .andExpect((jsonPath("$.description").value(equalTo("Cardiology"))));
    }

    @Test
    @DisplayName("Test update specialty with valid id")
    void testUpdateSpecialtyWithValidId() throws Exception {
        UpdateSpecialtyDto updateSpecialtyDto = new UpdateSpecialtyDto(2L, "Dermatology", "Dermatology");
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(2L, "Dermatology", "Dermatology");
        when(specialtyService.updateSpecialty(any(UpdateSpecialtyDto.class))).thenReturn(infoDto);
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));

        mockMvc.perform(put("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSpecialtyDto)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(2))))
                .andExpect((jsonPath("$.name").value(equalTo("Dermatology"))))
                .andExpect((jsonPath("$.description").value(equalTo("Dermatology"))));
    }

    @Test
    @DisplayName("Test update specialty with invalid id")
    void testUpdateSpecialtyWithInvalidId() throws Exception {
        UpdateSpecialtyDto updateSpecialtyDto = new UpdateSpecialtyDto(2L, "Dermatology", "Dermatology");
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSpecialtyDto)))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No specialty with the given ID found"))));
    }

    @Test
    @DisplayName("Test delete specialty with valid id")
    void testDeleteSpecialtyWithValidId() throws Exception {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(SpecialtyEntity.class)));

        mockMvc.perform(delete("/specialties/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message").value(equalTo("Specialty with the given ID deleted"))));
    }

    @Test
    @DisplayName("Test delete specialty with invalid id")
    void testDeleteSpecialtyWithInvalidId() throws Exception {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/specialties/{id}", 1))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No specialty with the given ID found"))));
    }
}