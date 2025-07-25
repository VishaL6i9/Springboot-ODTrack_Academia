package com.academicaffairs.odtrack.service;

import com.academicaffairs.odtrack.entity.Timetable;
import com.academicaffairs.odtrack.entity.User;
import com.academicaffairs.odtrack.exception.ResourceNotFoundException;
import com.academicaffairs.odtrack.payload.CreateTimetableRequest;
import com.academicaffairs.odtrack.payload.TimetableDto;
import com.academicaffairs.odtrack.payload.UpdateTimetableRequest;
import com.academicaffairs.odtrack.repository.TimetableRepository;
import com.academicaffairs.odtrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;

    @Transactional
    public TimetableDto createTimetable(CreateTimetableRequest request) {
        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getStaffId()));

        Timetable timetable = Timetable.builder()
                .staff(staff)
                .date(request.getDate())
                .period(request.getPeriod())
                .year(request.getYear())
                .hall(request.getHall())
                .subject(request.getSubject())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Timetable savedTimetable = timetableRepository.save(timetable);
        return mapToDto(savedTimetable);
    }

    public TimetableDto getTimetableById(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable", "id", id));
        return mapToDto(timetable);
    }

    public List<TimetableDto> getAllTimetables(LocalDate date, Integer year) {
        List<Timetable> timetables;
        if (date != null && year != null) {
            // Assuming a method like findByDateAndYear exists in repository
            // For now, fetching all and filtering in memory (not ideal for large datasets)
            timetables = timetableRepository.findAll().stream()
                    .filter(t -> t.getDate().equals(date) && t.getYear().equals(year))
                    .collect(Collectors.toList());
        } else if (date != null) {
            timetables = timetableRepository.findAll().stream()
                    .filter(t -> t.getDate().equals(date))
                    .collect(Collectors.toList());
        } else if (year != null) {
            timetables = timetableRepository.findAll().stream()
                    .filter(t -> t.getYear().equals(year))
                    .collect(Collectors.toList());
        } else {
            timetables = timetableRepository.findAll();
        }
        return timetables.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public TimetableDto updateTimetable(Long id, UpdateTimetableRequest request) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable", "id", id));

        if (request.getStaffId() != null) {
            User staff = userRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getStaffId()));
            timetable.setStaff(staff);
        }
        if (request.getDate() != null) {
            timetable.setDate(request.getDate());
        }
        if (request.getPeriod() != null) {
            timetable.setPeriod(request.getPeriod());
        }
        if (request.getYear() != null) {
            timetable.setYear(request.getYear());
        }
        if (request.getHall() != null) {
            timetable.setHall(request.getHall());
        }
        if (request.getSubject() != null) {
            timetable.setSubject(request.getSubject());
        }
        if (request.getDayOfWeek() != null) {
            timetable.setDayOfWeek(request.getDayOfWeek());
        }
        if (request.getStartTime() != null) {
            timetable.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            timetable.setEndTime(request.getEndTime());
        }
        timetable.setUpdatedAt(LocalDateTime.now());

        Timetable updatedTimetable = timetableRepository.save(timetable);
        return mapToDto(updatedTimetable);
    }

    @Transactional
    public void deleteTimetable(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable", "id", id));
        timetableRepository.delete(timetable);
    }

    private TimetableDto mapToDto(Timetable timetable) {
        return new TimetableDto(
                timetable.getId(),
                timetable.getStaff().getId(),
                timetable.getDate(),
                timetable.getPeriod(),
                timetable.getYear(),
                timetable.getHall(),
                timetable.getSubject(),
                timetable.getDayOfWeek(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );
    }
}
