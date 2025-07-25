package com.academicaffairs.odtrack.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableCsvDto {
    private String staffEmail;
    private String date;
    private String period;
    private String year;
    private String hall;
    private String subject;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
