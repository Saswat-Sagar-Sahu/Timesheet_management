package com.timesheet.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitTimesheetRequest {
    private Integer timesheetId;
    private Integer contractorId;
    private String comment;
}

