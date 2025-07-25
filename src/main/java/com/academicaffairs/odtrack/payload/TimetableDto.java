package com.academicaffairs.odtrack.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableDto {
    private Long id;
    private Long staffId;
    private LocalDate date;
    private Integer period;
    private Integer year;
    private String hall;
    private String subject;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
