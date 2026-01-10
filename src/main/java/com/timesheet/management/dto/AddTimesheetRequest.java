package com.timesheet.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTimesheetRequest {
    private Integer contractorId;
    private LocalDate startDate;
    private List<TimesheetEntryDTO> entries;
}

