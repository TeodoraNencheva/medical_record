package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.service.ResponseService;
import com.rewe.medical_record.service.VisitService;
import com.rewe.medical_record.validation.ExistingVisitId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/visit")
public class VisitController {
    private final VisitService visitService;
    @GetMapping
    public ResponseEntity<List<VisitInfoDto>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitInfoDto> getVisitInfo(@ExistingVisitId @PathVariable Long id) {
        return ResponseEntity.ok(visitService.getVisitInfo(id));
    }

    @PostMapping
    public ResponseEntity<VisitInfoDto> addVisit(@Valid @RequestBody AddVisitDto addVisitDto) {
        return new ResponseEntity<>(visitService.addVisit(addVisitDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<VisitInfoDto> updateVisit(@Valid @RequestBody UpdateVisitDto updateVisitDto) {
        return ResponseEntity.ok(visitService.updateVisit(updateVisitDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVisit(@ExistingVisitId @PathVariable Long id) {
        visitService.deleteVisit(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse("Visit with the given ID deleted");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
