package com.academicaffairs.odtrack.payload;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimetableRequest {
    private Long staffId;
    private LocalDate date;
    @Min(value = 1, message = "Period must be at least 1")
    private Integer period;
    @Min(value = 1, message = "Year must be at least 1")
    private Integer year;
    private String hall;
    private String subject;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
