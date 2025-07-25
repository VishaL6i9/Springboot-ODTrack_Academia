package com.academicaffairs.odtrack.controller;

import com.academicaffairs.odtrack.payload.CreateTimetableRequest;
import com.academicaffairs.odtrack.payload.TimetableDto;
import com.academicaffairs.odtrack.payload.UpdateTimetableRequest;
import com.academicaffairs.odtrack.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping
    public ResponseEntity<TimetableDto> createTimetable(@Valid @RequestBody CreateTimetableRequest request) {
        TimetableDto newTimetable = timetableService.createTimetable(request);
        return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableDto> getTimetableById(@PathVariable Long id) {
        TimetableDto timetable = timetableService.getTimetableById(id);
        return ResponseEntity.ok(timetable);
    }

    @GetMapping
    public ResponseEntity<List<TimetableDto>> getAllTimetables(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer year) {
        List<TimetableDto> timetables = timetableService.getAllTimetables(date, year);
        return ResponseEntity.ok(timetables);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<TimetableDto> updateTimetable(@PathVariable Long id, @Valid @RequestBody UpdateTimetableRequest request) {
        TimetableDto updatedTimetable = timetableService.updateTimetable(id, request);
        return ResponseEntity.ok(updatedTimetable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return ResponseEntity.ok("Timetable entry deleted successfully.");
    }
}
