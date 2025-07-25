package com.academicaffairs.odtrack.repository;

import com.academicaffairs.odtrack.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}
