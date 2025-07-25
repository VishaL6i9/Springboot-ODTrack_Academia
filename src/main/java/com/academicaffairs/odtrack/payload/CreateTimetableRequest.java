package com.academicaffairs.odtrack.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimetableRequest {
    @NotNull(message = "Staff ID cannot be null")
    private Long staffId;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Period cannot be null")
    @Min(value = 1, message = "Period must be at least 1")
    private Integer period;

    @NotNull(message = "Year cannot be null")
    @Min(value = 1, message = "Year must be at least 1")
    private Integer year;

    @NotBlank(message = "Hall cannot be blank")
    private String hall;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotBlank(message = "Day of week cannot be blank")
    private String dayOfWeek;

    @NotBlank(message = "Start time cannot be blank")
    private String startTime;

    @NotBlank(message = "End time cannot be blank")
    private String endTime;
}
