package com.timesheet.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetEntryDTO {
    private Integer projectId;
    private Integer activityCode;
    private LocalDate date;
    private BigDecimal hoursWorked;
    private String comments;
}

